package dockerregistry.blobs;

import dockerregistry.internal.rest.ResponseBuilder;
import org.jboss.resteasy.reactive.RestHeader;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/v2/{name}/blobs")
public class BlobsResource {

    @Inject
    BlobsService blobsService;

    @Path("/{digest}")
    @HEAD
    public Response exists(@PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobsService.layerExists(name, digest)) {
            return ResponseBuilder.ok()
                    .dockerContentDigest(digest)
                    .contentLength(blobsService.getLayerSize(digest))
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @Path("/{digest}")
    //@Produces({ "application/octet-stream" })
    @GET
    public Response download(@PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobsService.layerExists(name, digest)) {
            var length = blobsService.getLayerSize(digest);

            return ResponseBuilder.ok(blobsService.getLayer(name, digest))
                    .contentLength(length)
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @Path("/uploads")
    @POST
    public Response startUpload(@PathParam("name") String name, @QueryParam("digest") String digest) {
        var guid = blobsService.getUniqueId(name, digest);
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
        long position  = blobsService.uploadLayer(range, uuid, body);

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
            blobsService.finishUpload(uuid, digest, range, body);
        }

        blobsService.finishUpload(uuid, digest);

        return ResponseBuilder.ok().dockerContentDigest(uuid).build();
    }
}
