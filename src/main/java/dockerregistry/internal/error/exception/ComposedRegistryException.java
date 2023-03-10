package dockerregistry.internal.error.exception;

import dockerregistry.internal.error.model.Error;
import dockerregistry.internal.error.model.Errors;

import java.util.ArrayList;
import java.util.List;

public class ComposedRegistryException extends RuntimeException {

    private List<RegistryException> exceptions;

    ComposedRegistryException(List<RegistryException> exceptions) {
        this.exceptions = exceptions;
    }

    public Errors getErrors() {
        return new Errors(exceptions.stream()
                .map(RegistryException::getError)
                .toArray(Error[]::new));
    }

    public static Builder of() {
        return new Builder();
    }

    public static Builder from(ComposedRegistryException exception) {
        return new Builder(exception.exceptions);
    }

    static class Builder {
        private List<RegistryException> exceptions;

        public Builder() {
            this.exceptions = new ArrayList<>();
        }

        public Builder(List<RegistryException> exceptions) {
            this.exceptions = exceptions;
        }

        public Builder add(RegistryException exception) {
            exceptions.add(exception);

            return this;
        }

        public ComposedRegistryException build() {
            return new ComposedRegistryException(exceptions);
        }
    }
}
