package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class ManifestInvalidException extends RegistryException {

    public ManifestInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_INVALID.getError();
    }
}
