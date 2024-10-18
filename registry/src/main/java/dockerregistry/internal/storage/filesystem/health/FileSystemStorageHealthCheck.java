package dockerregistry.internal.storage.filesystem.health;

import dockerregistry.internal.config.RegistryConfiguration;
import jakarta.inject.Inject;
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

    @Inject
    RegistryConfiguration configuration;

    @Override
    public HealthCheckResponse call() {
        var builder = HealthCheckResponse.builder()
                .name("File System Storage");

        if(Path.of(configuration.storage().location()).toFile().exists()) {
            builder.up();
        } else {
            builder.down();
        }

        return builder
                .build();
    }
}
