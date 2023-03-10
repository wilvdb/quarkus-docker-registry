package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class DigestInvalidException extends RegistryException {

    public DigestInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.DIGEST_INVALID.getError();
    }
}
