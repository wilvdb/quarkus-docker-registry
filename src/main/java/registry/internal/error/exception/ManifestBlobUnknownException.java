package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class ManifestBlobUnknownException extends AbstractRegistryException {

    public ManifestBlobUnknownException() {
        super();
    }

    public ManifestBlobUnknownException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_BLOB_UNKNOWN.getError();
    }
}
