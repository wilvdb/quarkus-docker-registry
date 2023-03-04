package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class RangeInvalidException extends RegistryException {

    public RangeInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.RANGE_INVALID.getError();
    }
}
