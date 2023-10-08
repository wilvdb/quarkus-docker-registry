package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public abstract class RegistryException extends RuntimeException {

    public RegistryException() {
        super();
    }

    public RegistryException(Throwable throwable) {
        super(throwable);
    }
    public abstract Error getError();
}
