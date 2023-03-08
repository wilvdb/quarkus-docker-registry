package dockerregistry.blobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dockerregistry.internal.error.exception.BlobUploadInvalidException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class LayerService {

    private static final Logger logger = LoggerFactory.getLogger(LayerService.class);

    private Path path;

    @PostConstruct
    protected void createRegistryDirectory() throws IOException {
        path = Files.createTempDirectory("tmpRegistry");
    }

    public String getUniqueId(String name, String digest) {
        return UUID.randomUUID().toString();
    }

    public long getLayerSize(String digest) {
        var hash = digest.split(":")[1];
        try {
            return Files.size(path.resolve(hash));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean layerExists(String name, String digest) {
        logger.debug("Check if image {} with digest {} is present", name, digest);

        var hash = digest.split(":")[1];

        return path.resolve(hash).toFile().exists();
    }

    public long uploadLayer(String range, String uuid, InputStream inputStream) {
        logger.debug("Upload blob from range {} UUID {}", range, uuid);

        var start = Optional.ofNullable(range)
                .map(r -> r.split("-")[0])
                .map(Long::parseLong)
                .orElse(0l);

        var filePath = path.resolve(uuid);

        try (var outputChannel = Files.newByteChannel(filePath, StandardOpenOption.APPEND, StandardOpenOption.CREATE).position(start)) {
            logger.debug("Write to file {}", filePath);
            outputChannel.write(ByteBuffer.wrap(inputStream.readAllBytes()));

            logger.debug("finish upload the layer");

            return outputChannel.position();

        } catch (IOException e) {
            throw new BlobUploadInvalidException(e);
        }

    }

    /**
     * TODO read inputstream if there is one
     * @param uuid
     * @param digest
     */
    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];
        var target = path.resolve(hash).toFile();

        logger.debug("Rename layer {} to {}", uuid, hash);

        path.resolve(uuid).toFile().renameTo(target);
    }

    public void finishUpload(String uuid, String digest, String range, InputStream body) {
        uploadLayer(range, uuid, body);

        finishUpload(uuid, digest);
    }

    public byte[] getLayer(String name, String digest) {
        logger.debug("Get layer {} for image {}", digest, name);

        var hash = digest.split(":")[1];
        var target =  path.resolve(hash);

        try {
            logger.debug("Read file {}", target);

            return Files.readAllBytes(target);

        } catch (IOException e) {
            throw new BlobUploadInvalidException(e);
        }
    }

}
