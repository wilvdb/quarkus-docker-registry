package dockerregistry.base;

import dockerregistry.internal.rest.ResponseBuilder;
import io.quarkus.logging.Log;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    @RolesAllowed({"read", "write"})
    @GET
    public Response getApiVersion() {
        Log.info("Get API version");

        return ResponseBuilder.ok().dockerDistributionApiVersion().build();
    }
}
