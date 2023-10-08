package dockerregistry.referrer;

import dockerregistry.internal.error.exception.UnsupportedException;
import dockerregistry.internal.validation.Namespace;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/v2/{name}/referrers/{digest}")
public class ReferrerResource {

    @GET
    public Response getReferrers(@Namespace @PathParam("name") String name, @PathParam("digest") String digest, @QueryParam("artifactType") String artifactType) {
        throw new UnsupportedException();
    }
}
