package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class BlobUnknownException extends AbstractRegistryException {

    public BlobUnknownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.BLOB_UNKNOWN.getError();
    }
}
