package dockerregistry.internal.storage.filesystem.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import java.nio.file.Path;

@Liveness
@Readiness
//@ApplicationScoped
public class FileSystemStorageHealthCheck implements HealthCheck {

    @ConfigProperty(name = "registry.location", defaultValue = "/tmp")
    Path location;

    @Override
    public HealthCheckResponse call() {
        var builder = HealthCheckResponse.builder()
                .name("File System Storage");

        if(location.toFile().exists()) {
            builder.up();
        } else {
            builder.down();
        }

        return builder
                .build();
    }
}
