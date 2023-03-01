package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class NameInvalidException extends AbstractRegistryException {

    public NameInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.NAME_INVALID.getError();
    }
}
