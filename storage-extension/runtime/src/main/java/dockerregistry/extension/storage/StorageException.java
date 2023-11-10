package dockerregistry.extension.storage;

public abstract class StorageException extends RuntimeException {

    public StorageException(Throwable cause) {
        super(cause);
    }
}
