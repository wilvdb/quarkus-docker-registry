package dockerregistry.extension.storage.filesystem;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;

@ConfigRoot(prefix = "registry.storage.filesystem", phase = ConfigPhase.RUN_TIME)
public class FileSystemStorageConfiguration {

    /**
     * Location of the root file system.
     */
    @ConfigItem
    public Optional<String> location;
}
