package dockerregistry.manifest;

import dockerregistry.internal.error.exception.NameUnknownException;
import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.rest.ResponseBuilder;
import dockerregistry.internal.validation.Namespace;
import dockerregistry.internal.validation.Reference;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.Optional;

@Path("/v2/{name}/manifests/{reference}")
public class ManifestResource {

    @Inject
    ManifestService manifestService;

    @RolesAllowed({"read"})
    @GET
    public Response download(@Namespace @PathParam("name") String name, @Reference @PathParam("reference") String reference) {
        return manifestService.getManifest(name, reference).
                map(manifest -> ResponseBuilder.ok()
                        .dockerContentDigest(manifest.digest())
                        .contentLength(manifest.length())
                        .contentType(manifest.mediaType())
                        .build())
                .orElseThrow(NameUnknownException::new);
    }

    @RolesAllowed({"write"})
    @PUT
    public Response upload(@Namespace @PathParam("name") String name, @Reference @PathParam("reference") String reference, InputStream body) {
        var manifest = manifestService.saveManifest(name, reference, body);

        return ResponseBuilder.created(String.format("/v2/%s/manifests/%s", name, reference))
                .dockerContentDigest(manifest.digest())
                .build();
    }

    @RolesAllowed({"read"})
    @HEAD
    public Response existsManifest(@Namespace  @PathParam("name") String name, @Reference @PathParam("reference") String reference) {
        return manifestService.getManifest(name, reference)
                .map(manifest -> ResponseBuilder.ok()
                        .dockerContentDigest(manifest.digest())
                        .contentLength(manifest.length())
                        .contentType(manifest.mediaType())
                        .build())
                .or(() -> Optional.of(ResponseBuilder.notFound().build()))
                .get();
    }

    @RolesAllowed({"write"})
    @DELETE
    public Response delete(@Namespace @PathParam("name") String name, @Reference @PathParam("reference") String reference) {
        throw new UnsupportedException();
    }
}