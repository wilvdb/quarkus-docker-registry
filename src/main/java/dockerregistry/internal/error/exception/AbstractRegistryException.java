package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public abstract class AbstractRegistryException extends RuntimeException {

    public AbstractRegistryException() {
        super();
    }

    public AbstractRegistryException(Throwable throwable) {
        super(throwable);
    }
    public abstract Error getError();
}
