package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class NameUnknownException extends AbstractRegistryException {

    public NameUnknownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.NAME_UNKNOWN.getError();
    }
}
