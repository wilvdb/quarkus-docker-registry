package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class TagInvalidException extends RegistryException {

    public TagInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.TAG_INVALID.getError();
    }
}
