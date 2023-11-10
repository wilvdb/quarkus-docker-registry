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
        var path = configuration.location
                .map(Path::of)
                .orElseThrow(IllegalArgumentException::new);

        if(!path.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Path [%s] dpes not exist", path.toString()));
        }

        return new FileSystemStorage(path);
    }
}
