package dockerregistry.extension.storage.filesystem;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Dependent
public class FileSystemRootProvider {

    @Produces
    public Path createFileSystemRoot() throws IOException {
        return Files.createTempDirectory("tmpRegistry");
    }
}
