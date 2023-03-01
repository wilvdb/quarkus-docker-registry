package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class BlobUploadUnkownException extends AbstractRegistryException {

    public BlobUploadUnkownException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.BLOB_UPLOAD_UNKNOWN.getError();
    }
}
