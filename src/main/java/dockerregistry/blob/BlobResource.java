package dockerregistry.blob;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.rest.ResponseBuilder;
import dockerregistry.internal.validation.Namespace;
import dockerregistry.internal.validation.Range;
import jakarta.annotation.security.RolesAllowed;
import org.jboss.resteasy.reactive.RestHeader;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/v2/{name}/blobs")
public class BlobResource {

    @Inject
    BlobService blobService;

    @RolesAllowed({"read"})
    @Path("/{digest}")
    @HEAD
    public Response exists(@Namespace @PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobService.layerExists(name, digest)) {
            return ResponseBuilder.ok()
                    .dockerContentDigest(digest)
                    .contentLength(blobService.getLayerSize(digest))
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @RolesAllowed({"read"})
    @Path("/{digest}")
    @GET
    public Response download(@Namespace @PathParam("name") String name, @PathParam("digest") String digest) {
        if(blobService.layerExists(name, digest)) {
            var length = blobService.getLayerSize(digest);

            return ResponseBuilder.ok(blobService.getLayer(name, digest))
                    .contentLength(length)
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }

    @RolesAllowed({"write"})
    @Path("/{digest}")
    @DELETE
    public Response delete(@Namespace @PathParam("name") String name, @PathParam("digest") String digest) {
        throw new UnsupportedException();
    }

    @RolesAllowed({"write"})
    @Path("/uploads")
    @POST
    public Response startUpload(@Namespace @PathParam("name") String name, @QueryParam("digest") String digest) {
        var guid = blobService.getUniqueId(name, digest);
        return ResponseBuilder.accepted()
                .location("/v2/" + name + "/blobs/uploads/" + guid)
                .range(0)
                .contentLength(0)
                .dockerUploadUuid(guid)
                .build();
    }

    @RolesAllowed({"write"})
    @Path("/uploads/{uuid}")
    @PATCH
    public Response upload(@Namespace @PathParam("name") String name, @PathParam("uuid") String uuid, @Range @RestHeader("Content-Range") String range, InputStream body) {
        long position  = blobService.uploadLayer(range, uuid, body);

        return ResponseBuilder.accepted()
            .location("/v2/" + name + "/blobs/uploads/" + uuid)
            .range(position - 1)
            .contentLength(0)
            .dockerUploadUuid(uuid)
            .build();
    }

    @RolesAllowed({"write"})
    @Path("/uploads/{uuid}")
    @PUT
    public Response finishUpload(@Namespace @PathParam("name") String name, @PathParam("uuid") String uuid, @QueryParam("digest") String digest, @Range @RestHeader("Content-Range") String range, @RestHeader("Content-Length") long length, InputStream body) {
        if(length != 0) {
            blobService.finishUpload(uuid, digest, range, body);
        }

        blobService.finishUpload(uuid, digest);

        return ResponseBuilder.ok().dockerContentDigest(uuid).build();
    }
}
