package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class ManifestUnknownException extends AbstractRegistryException {

    public ManifestUnknownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_UNKNOWN.getError();
    }
}
