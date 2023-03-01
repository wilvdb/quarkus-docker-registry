package registry.base;

import io.quarkus.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v2")
public class BaseResource {

    @GET
    public Response getApiVersion() {
        Log.info("Get API version");
        return Response.ok().header("Docker-Distribution-API-Version", "registry/2.0").build();
    }
}
