package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class PaginationNumberInvalidException extends RegistryException {

    public PaginationNumberInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.PAGINATION_NUMBER_INVALID.getError();
    }
}
