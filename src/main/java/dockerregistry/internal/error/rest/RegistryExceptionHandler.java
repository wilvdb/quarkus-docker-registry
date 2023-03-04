package dockerregistry.internal.error.rest;

import dockerregistry.internal.error.exception.RegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dockerregistry.internal.error.model.Errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RegistryExceptionHandler implements ExceptionMapper<RegistryException> {

    private static final Logger logger = LoggerFactory.getLogger(RegistryExceptionHandler.class);

    @Override
    public Response toResponse(RegistryException e) {
        logger.error("Handle exception", e);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Errors(e.getError()))
                .build();
    }
}
