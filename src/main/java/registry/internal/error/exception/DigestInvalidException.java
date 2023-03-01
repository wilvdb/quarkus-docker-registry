package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class DigestInvalidException extends AbstractRegistryException {

    public DigestInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.DIGEST_INVALID.getError();
    }
}
