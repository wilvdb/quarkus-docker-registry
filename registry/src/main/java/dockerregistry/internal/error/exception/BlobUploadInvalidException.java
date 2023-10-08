package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class BlobUploadInvalidException extends RegistryException {

    public BlobUploadInvalidException() {
    }

    public BlobUploadInvalidException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.BLOB_UPLOAD_INVALID.getError();
    }
}
