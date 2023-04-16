package dockerregistry.base;

import dockerregistry.internal.rest.ResponseBuilder;
import io.quarkus.logging.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    @GET
    public Response getApiVersion() {
        Log.info("Get API version");

        return ResponseBuilder.ok().dockerDistributionApiVersion().build();
    }
}
