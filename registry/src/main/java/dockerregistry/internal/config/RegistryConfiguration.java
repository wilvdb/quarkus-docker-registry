package dockerregistry.internal.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.time.Duration;

@StaticInitSafe
@ConfigMapping(prefix = "registry")
public interface RegistryConfiguration {

    TooManyRequest tooManyRequest();

    interface TooManyRequest {

        @WithDefault("100")
        int limit();

        @WithDefault("1S")
        Duration timestamp();
    }
}
