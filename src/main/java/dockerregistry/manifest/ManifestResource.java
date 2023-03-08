package dockerregistry.manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;

@Path("/v2/{name}/manifests/{reference}")
public class ManifestResource {

    private static Logger logger = LoggerFactory.getLogger(ManifestResource.class);

    @Inject
    ManifestService manifestService;

    @GET
    //@Produces({ "application/octet-stream" })
    public Response download(@PathParam("name") String name, @PathParam("reference") String reference) {
        logger.info("Get manifest with name {} and reference {}", name, reference);

        manifestService.checkExistence(name, reference);

        return Response.ok(manifestService.getContent(name, reference))
                    .header("docker-content-digest", "sha256:" + manifestService.getSha256(name, reference))
                    .header("content-type", manifestService.getMediaType(name, reference))
                    .header("content-length", manifestService.getContentLength(name, reference))
                    .build();
    }

    @PUT
    public Response saveManifest(@PathParam("name") String name, @PathParam("reference") String reference, InputStream body) {
        return Response.created(URI.create(String.format("/v2/%s/manifests/%s", name, reference)))
                .header("Docker-Content-Digest", "sha256:" + manifestService.saveManifest(name, reference, body))
                .build();
    }

    @HEAD
    public Response existsManifest(@PathParam("name") String name, @PathParam("reference") String reference) {
        if(manifestService.manifestExists(name, reference)) {
            return Response.ok()
                    .header("Docker-Content-Digest", manifestService.getSha256(name, reference))
                    .header("Content-Length", manifestService.getContentLength(name, reference))
                    .header("Content-Type", manifestService.getMediaType(name, reference))
                    .build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}