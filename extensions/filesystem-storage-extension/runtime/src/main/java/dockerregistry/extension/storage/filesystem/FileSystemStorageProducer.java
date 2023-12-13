package dockerregistry.extension.storage.filesystem;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.nio.file.Path;

@ApplicationScoped
public class FileSystemStorageProducer {

    @Inject
    FileSystemStorageConfiguration configuration;

    @Singleton
    @Default
    @Produces
    public FileSystemStorage fileSystemStorage() {
        if(configuration.location() == null) {
            throw new IllegalArgumentException("Missing location");
        }

        var path = Path.of(configuration.location());

        if(!path.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Path [%s] dpes not exist", path.toString()));
        }

        return new FileSystemStorage(path);
    }
}
