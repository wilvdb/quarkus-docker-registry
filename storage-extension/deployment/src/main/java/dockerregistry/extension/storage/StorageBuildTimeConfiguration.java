package dockerregistry.extension.storage;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "registry.storage")
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface StorageBuildTimeConfiguration {

    /**
     * Enable health check.
     * @return health check enabled
     */
    @WithName("health.enabled")
    @WithDefault("true")
    boolean healthEnabled();
}
