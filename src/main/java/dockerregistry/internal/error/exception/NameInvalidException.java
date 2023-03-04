package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class NameInvalidException extends RegistryException {

    public NameInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.NAME_INVALID.getError();
    }
}
