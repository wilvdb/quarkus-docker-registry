package dockerregistry.extension.storage.filesystem;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

public class FileSystemStorageExtensionProcessor {

    private static final String FEATURE = "filesystem-storage-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
