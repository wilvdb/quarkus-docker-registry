package dockerregistry.blobs;

import org.jboss.resteasy.reactive.RestHeader;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/v2/{name}/blobs")
public class BlobsResource {

    @Inject
    LayerService layerService;

    @Path("/{digest}")
    @HEAD
    public Response exists(@PathParam("name") String name, @PathParam("digest") String digest) {
        if(layerService.layerExists(name, digest)) {
            var length = layerService.getLayerSize(digest);
            return Response.ok()
                    .header("Docker-Content-Digest", digest)
                    .header("Content-Length", length)
                    .build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/uploads")
    @POST
    public Response startUpload(@PathParam("name") String name, @QueryParam("digest") String digest) {
        var builder = Response.accepted();

        var guid = layerService.getUniqueId(name, digest);
        builder.header("location", "/v2/" + name + "/blobs/uploads/" + guid);
        builder.header("range", "0-0");
        builder.header("content-length", "0");
        builder.header("docker-upload-uuid", guid);
        return builder.build();
    }

    @Path("/uploads/{uuid}")
    @PATCH
    public Response upload(@PathParam("name") String name, @PathParam("uuid") String uuid, @RestHeader("Content-Range") String range, InputStream body) {
        long position  = layerService.uploadLayer(range, uuid, body);

        return Response.accepted()
            .header("location", "/v2/" + name + "/blobs/uploads/" + uuid)
            .header("range", "0-" + (position - 1))
            .header("content-length", "0")
            .header("docker-upload-uuid", uuid)
            .build();
    }

    @Path("/uploads/{uuid}")
    @PUT
    public Response finishUpload(@PathParam("name") String name, @PathParam("uuid") String uuid, @QueryParam("digest") String digest, @RestHeader("Content-Range") String range, @RestHeader("Content-Length") long length, InputStream body) {
        if(length != 0) {
            layerService.finishUpload(uuid, digest, range, body);
        }

        layerService.finishUpload(uuid, digest);

        return Response.ok().header("Docker-Content-Digest", uuid).build();
    }
}
