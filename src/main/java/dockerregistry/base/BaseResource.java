package dockerregistry.base;

import dockerregistry.internal.rest.ResponseBuilder;
import io.quarkus.logging.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    static final String DOCKER_VERSION_HEADER = "Docker-Distribution-API-Version";
    static final String DOCKER_REGISTRY_VERSION = "dockerregistry/2.0";

    @GET
    public Response getApiVersion() {
        Log.info("Get API version");

        return ResponseBuilder.ok().dockerDistributionApiVersion().build();
    }
}
