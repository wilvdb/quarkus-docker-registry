package dockerregistry.extension.storage.filesystem;

import dockerregistry.extension.storage.StorageBuildTimeConfiguration;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.smallrye.health.deployment.spi.HealthBuildItem;

public class FileSystemStorageBuildSteps {

    private static final String FEATURE = "filesystem-storage-extension";

    @BuildStep
    public FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    public AdditionalBeanBuildItem producer() {
        return new AdditionalBeanBuildItem(FileSystemStorageProducer.class);
    }

//    @BuildStep
//    public HealthBuildItem addHealthCheck(StorageBuildTimeConfiguration storageBuildTimeConfiguration) {
//        if (capabilities.isPresent(Capability.SMALLRYE_HEALTH)) {
//            return new HealthBuildItem("dockerregistry.extension.storage.filesystem.health.FilesystemStorageHealthCheck",
//                    storageBuildTimeConfiguration.healthEnabled());
//        } else {
//            return null;
//        }
//    }
}
