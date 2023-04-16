package dockerregistry.base;

import dockerregistry.internal.rest.ResponseBuilder;
import io.quarkus.logging.Log;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    @GET
    public Response getApiVersion() {
        Log.info("Get API version");

        return ResponseBuilder.ok().dockerDistributionApiVersion().build();
    }
}
