package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class BlobUploadInvalidException extends AbstractRegistryException {

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
