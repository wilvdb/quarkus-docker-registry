package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class BlobUploadUnkownException extends AbstractRegistryException {

    public BlobUploadUnkownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.BLOB_UPLOAD_UNKNOWN.getError();
    }
}
