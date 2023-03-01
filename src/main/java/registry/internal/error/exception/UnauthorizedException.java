package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class UnauthorizedException extends AbstractRegistryException {

    public UnauthorizedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.UNAUTHORIZED.getError();
    }
}
