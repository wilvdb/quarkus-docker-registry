package dockerregistry.extension.storage.filesystem;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "registry.filesystem.storage")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface FileSystemStorageConfiguration {

        /**
         * Location of the storage
         * @return storage location
         */
        String location();

}
