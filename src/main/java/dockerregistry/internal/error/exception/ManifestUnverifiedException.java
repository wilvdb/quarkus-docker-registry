package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;

public class ManifestUnverifiedException extends RegistryException {

    public ManifestUnverifiedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_UNVERIFIED.getError();
    }
}
