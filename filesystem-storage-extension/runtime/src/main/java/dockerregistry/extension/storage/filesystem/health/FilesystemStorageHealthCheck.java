package dockerregistry.extension.storage.filesystem.health;

import dockerregistry.extension.storage.filesystem.FileSystemStorageConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import java.nio.file.Path;

@Liveness
@Readiness
@ApplicationScoped
public class FilesystemStorageHealthCheck implements HealthCheck {

    @Inject
    FileSystemStorageConfiguration configuration;

    @Override
    public HealthCheckResponse call() {
        var builder = HealthCheckResponse.builder()
                .name("File System Storage");

        if(configuration.location() == null) {
            throw new IllegalArgumentException("Missing location");
        }

        var path = Path.of(configuration.location());

        if(path.toFile().exists()) {
            builder.up();
        } else {
            builder.down();
        }

        return builder
                .build();
    }
}
