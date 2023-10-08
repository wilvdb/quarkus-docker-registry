package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class ManifestInvalidException extends RegistryException {

    public ManifestInvalidException() {
        super();
    }

    public ManifestInvalidException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_INVALID.getError();
    }
}
