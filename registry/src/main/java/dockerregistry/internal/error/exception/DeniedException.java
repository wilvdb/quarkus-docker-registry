package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class DeniedException extends RegistryException {

    public DeniedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.DENIED.getError();
    }
}
