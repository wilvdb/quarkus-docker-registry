package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class UnsupportedException extends AbstractRegistryException {

    public UnsupportedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.UNSUPPORTED.getError();
    }
}
