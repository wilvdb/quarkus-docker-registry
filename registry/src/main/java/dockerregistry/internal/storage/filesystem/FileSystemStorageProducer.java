package dockerregistry.internal.storage.filesystem;

import dockerregistry.internal.storage.Storage;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;

public class FileSystemStorageProducer {

    @ConfigProperty(name = "registry.location", defaultValue = "/tmp")
    Path location;

    @ApplicationScoped
    public Storage fileSystemStorage() {
        Log.debugf("File system storage to %s", location.toString());
        if(!location.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Path [%s] does not exist", location.toString()));
        }

        return new FileSystemStorage(location);
    }
}
