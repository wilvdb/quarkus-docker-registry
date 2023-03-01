package registry.internal.error.exception;

import registry.internal.error.model.Error;

public class ManifestUnverifiedException extends AbstractRegistryException {

    public ManifestUnverifiedException() {
    }

    @Override
    public Error getError() {
        return ErrorIdentifier.MANIFEST_UNVERIFIED.getError();
    }
}
