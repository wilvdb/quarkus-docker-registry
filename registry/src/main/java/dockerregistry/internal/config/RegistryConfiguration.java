package dockerregistry.internal.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@StaticInitSafe
@ConfigMapping(prefix = "registry")
public interface RegistryConfiguration {

    TooManyRequest tooManyRequest();

    Storage storage();

    interface TooManyRequest {

        @WithDefault("100")
        int limit();

        @WithDefault("1S")
        Duration timestamp();
    }

    interface Storage {

        @WithDefault("/tmp")
        String location();
    }
}
