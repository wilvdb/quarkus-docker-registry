package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class UnsupportedException extends AbstractRegistryException {

    public UnsupportedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.UNSUPPORTED.getError();
    }
}
