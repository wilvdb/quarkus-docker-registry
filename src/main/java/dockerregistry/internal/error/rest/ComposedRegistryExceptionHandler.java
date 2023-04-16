package dockerregistry.internal.error.rest;

import dockerregistry.internal.error.exception.ComposedRegistryException;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ComposedRegistryExceptionHandler implements ExceptionMapper<ComposedRegistryException> {

    @Override
    public Response toResponse(ComposedRegistryException e) {
        Log.error("Handle exception", e);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getErrors())
                .build();
    }
}
