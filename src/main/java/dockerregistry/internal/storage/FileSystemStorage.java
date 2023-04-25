package dockerregistry.internal.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;

@ApplicationScoped
public class FileSystemStorage {

    @Inject
    Path root;


}
