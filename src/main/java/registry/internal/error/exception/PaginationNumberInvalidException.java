package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class PaginationNumberInvalidException extends AbstractRegistryException {

    public PaginationNumberInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.PAGINATION_NUMBER_INVALID.getError();
    }
}
