package dockerregistry.manifest;

import dockerregistry.internal.rest.ResponseBuilder;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/v2/{name}/manifests/{reference}")
public class ManifestResource {

    @Inject
    ManifestService manifestService;

    @GET
    //@Produces({ "application/octet-stream" })
    public Response download(@PathParam("name") String name, @PathParam("reference") String reference) {
        manifestService.checkExistence(name, reference);

        return ResponseBuilder.ok(manifestService.getContent(name, reference))
                    .dockerContentDigest("sha256:" + manifestService.getSha256(name, reference))
                    .contentType(manifestService.getMediaType(name, reference))
                    .contentLength(manifestService.getContentLength(name, reference))
                    .build();
    }

    @PUT
    public Response upload(@PathParam("name") String name, @PathParam("reference") String reference, InputStream body) {
        return ResponseBuilder.created(String.format("/v2/%s/manifests/%s", name, reference))
                .dockerContentDigest("sha256:" + manifestService.saveManifest(name, reference, body))
                .build();
    }

    @HEAD
    public Response existsManifest(@PathParam("name") String name, @PathParam("reference") String reference) {
        if(manifestService.manifestExists(name, reference)) {
            return ResponseBuilder.ok()
                    .dockerContentDigest(manifestService.getSha256(name, reference))
                    .contentLength(manifestService.getContentLength(name, reference))
                    .contentType(manifestService.getMediaType(name, reference))
                    .build();
        }

        return ResponseBuilder.notFound().build();
    }
}