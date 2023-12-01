package dockerregistry.extension.storage.filesystem;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "registry.filesystem.storage")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface FileSystemStorageBuildTimeConfiguration {


        /**
         * Enable health check
         * @return health check enabled
         */
        @WithName("health.enabled")
        @WithDefault("true")
        boolean healthEnabled();
}
