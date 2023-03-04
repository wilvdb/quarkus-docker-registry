package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class ManifestBlobUnknownException extends RegistryException {

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
