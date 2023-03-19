package dockerregistry.tag;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.validation.Namespace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/v2/{name}/tags/list")
public class TagResource {

    @GET
    public Response getTags(@Namespace @PathParam("name") String name, @QueryParam("n") int count) {
        throw new UnsupportedException();
    }
}
