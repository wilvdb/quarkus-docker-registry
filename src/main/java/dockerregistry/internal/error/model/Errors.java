package dockerregistry.internal.error.model;

import java.util.Arrays;
import java.util.List;

public class Errors {

    private List<Error> errors;

    public Errors(Error... errors) {
        this.errors = Arrays.stream(errors).toList();
    }

    public List<Error> getErrors() {
        return errors;
    }
}
