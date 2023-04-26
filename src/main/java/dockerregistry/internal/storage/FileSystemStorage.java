package dockerregistry.internal.storage;

import dockerregistry.internal.error.exception.BlobUploadInvalidException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@ApplicationScoped
public class FileSystemStorage {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorage.class);

    @Inject
    Path root;

    public long uploadLayer(String range, String uuid, InputStream inputStream) {
        logger.debug("Upload blob from range {} UUID {}", range, uuid);

        var start = Optional.ofNullable(range)
                .map(r -> r.split("-")[0])
                .map(Long::parseLong)
                .orElse(0l);

        var filePath = root.resolve(uuid);

        try (var outputChannel = Files.newByteChannel(filePath, StandardOpenOption.APPEND, StandardOpenOption.CREATE).position(start)) {
            logger.debug("Write to file {}", filePath);
            outputChannel.write(ByteBuffer.wrap(inputStream.readAllBytes()));

            logger.debug("finish upload the layer");

            return outputChannel.position();

        } catch (IOException e) {
            throw new BlobUploadInvalidException(e);
        }

    }

    public byte[] getLayer(String name, String digest) {
        logger.debug("Get layer {} for image {}", digest, name);

        var hash = digest.split(":")[1];
        var target =  root.resolve(hash);

        try {
            logger.debug("Read file {}", target);

            return Files.readAllBytes(target);

        } catch (IOException e) {
            throw new BlobUploadInvalidException(e);
        }
    }

    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];
        var target = root.resolve(hash).toFile();

        logger.debug("Rename layer {} to {}", uuid, hash);

        root.resolve(uuid).toFile().renameTo(target);
    }

    public long getLayerSize(String hash) {
        try {
            return Files.size(root.resolve(hash));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
