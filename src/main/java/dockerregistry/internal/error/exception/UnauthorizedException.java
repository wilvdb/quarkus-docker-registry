package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class UnauthorizedException extends RegistryException {

    public UnauthorizedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.UNAUTHORIZED.getError();
    }
}
