package dockerregistry.referrer;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.validation.Namespace;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/v2/{name}/referrers/{digest}")
public class ReferrerResource {

    @GET
    public Response getReferrers(@Namespace @PathParam("name") String name, @PathParam("digest") String digest, @QueryParam("artifactType") String artifactType) {
        throw new UnsupportedException();
    }
}
