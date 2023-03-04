package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class TooManyRequestException extends RegistryException {

    @Override
    public Error getError() {
        return ErrorIdentifier.TOO_MANY_REQUEST.getError();
    }
}
