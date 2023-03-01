package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class ManifestInvalidException extends AbstractRegistryException {

    public ManifestInvalidException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_INVALID.getError();
    }
}
