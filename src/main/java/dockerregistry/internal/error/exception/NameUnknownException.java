package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class NameUnknownException extends RegistryException {

    public NameUnknownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.NAME_UNKNOWN.getError();
    }
}
