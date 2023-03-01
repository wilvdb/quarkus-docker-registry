package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class RangeInvalidException extends AbstractRegistryException {

    public RangeInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.RANGE_INVALID.getError();
    }
}
