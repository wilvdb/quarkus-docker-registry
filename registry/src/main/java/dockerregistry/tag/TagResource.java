package dockerregistry.tag;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.validation.Namespace;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/v2/{name}/tags/list")
public class TagResource {

    @GET
    public Response getTags(@Namespace @PathParam("name") String name, @QueryParam("n") int count) {
        throw new UnsupportedException();
    }
}
