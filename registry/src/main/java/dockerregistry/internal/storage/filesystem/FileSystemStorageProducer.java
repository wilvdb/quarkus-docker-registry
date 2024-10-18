package dockerregistry.internal.storage.filesystem;

import dockerregistry.internal.config.RegistryConfiguration;
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

@ApplicationScoped
public class FileSystemStorageProducer {

    @Inject
    RegistryConfiguration configuration;

    @ApplicationScoped
    public Storage fileSystemStorage() {
        var location = Path.of(configuration.storage().location());
        Log.debugf("File system storage to %s", location.toString());
        if(!location.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Path [%s] does not exist", location));
        }

        return new FileSystemStorage(location);
    }
}
