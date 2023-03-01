package dockerregistry.base;

import io.quarkus.logging.Log;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    @GET
    public Response getApiVersion() {
        Log.info("Get API version");
        return Response.ok().header("Docker-Distribution-API-Version", "dockerregistry/2.0").build();
    }
}
