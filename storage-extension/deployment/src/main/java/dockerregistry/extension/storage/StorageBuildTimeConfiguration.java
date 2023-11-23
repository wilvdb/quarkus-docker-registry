package dockerregistry.extension.storage;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "quarkus.storage", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class StorageBuildTimeConfiguration {

    @ConfigItem
    public boolean healthEnabled = true;
}
