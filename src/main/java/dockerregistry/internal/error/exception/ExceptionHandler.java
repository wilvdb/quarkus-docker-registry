package dockerregistry.internal.error.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dockerregistry.internal.error.model.Errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<AbstractRegistryException> {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public Response toResponse(AbstractRegistryException e) {
        logger.error("Handle exception", e);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Errors(e.getError()))
                .build();
    }
}
