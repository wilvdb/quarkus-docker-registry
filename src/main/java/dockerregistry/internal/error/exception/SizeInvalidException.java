package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class SizeInvalidException extends AbstractRegistryException {

    public SizeInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.SIZE_INVALID.getError();
    }
}
