package dockerregistry.blob;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.rest.ResponseBuilder;
import org.jboss.resteasy.reactive.RestHeader;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/v2/{name}/blobs")
public class BlobResource {

    @Inject
    BlobService blobService;

    @Path("/{digest}")
    @HEAD
    public Response exists(@PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobService.layerExists(name, digest)) {
            return ResponseBuilder.ok()
                    .dockerContentDigest(digest)
                    .contentLength(blobService.getLayerSize(digest))
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @Path("/{digest}")
    //@Produces({ "application/octet-stream" })
    @GET
    public Response download(@PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobService.layerExists(name, digest)) {
            var length = blobService.getLayerSize(digest);

            return ResponseBuilder.ok(blobService.getLayer(name, digest))
                    .contentLength(length)
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @Path("/{digest}")
    @DELETE
    public Response delete(@PathParam("name") String name, @PathParam("digest") String digest) {
        throw new UnsupportedException();
    }

    @Path("/uploads")
    @POST
    public Response startUpload(@PathParam("name") String name, @QueryParam("digest") String digest) {
        var guid = blobService.getUniqueId(name, digest);
        return ResponseBuilder.accepted()
                .location("/v2/" + name + "/blobs/uploads/" + guid)
                .range(0)
                .contentLength(0)
                .dockerUploadUuid(guid)
                .build();
    }

    @Path("/uploads/{uuid}")
    @PATCH
    public Response upload(@PathParam("name") String name, @PathParam("uuid") String uuid, @RestHeader("Content-Range") String range, InputStream body) {
        long position  = blobService.uploadLayer(range, uuid, body);

        return ResponseBuilder.accepted()
            .location("/v2/" + name + "/blobs/uploads/" + uuid)
            .range(position - 1)
            .contentLength(0)
            .dockerUploadUuid(uuid)
            .build();
    }

    @Path("/uploads/{uuid}")
    @PUT
    public Response finishUpload(@PathParam("name") String name, @PathParam("uuid") String uuid, @QueryParam("digest") String digest, @RestHeader("Content-Range") String range, @RestHeader("Content-Length") long length, InputStream body) {
        if(length != 0) {
            blobService.finishUpload(uuid, digest, range, body);
        }

        blobService.finishUpload(uuid, digest);

        return ResponseBuilder.ok().dockerContentDigest(uuid).build();
    }
}
