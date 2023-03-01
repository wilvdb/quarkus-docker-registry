package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class DeniedException extends AbstractRegistryException {

    public DeniedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.DENIED.getError();
    }
}
