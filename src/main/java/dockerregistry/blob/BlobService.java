package dockerregistry.blob;

import dockerregistry.internal.error.exception.BlobUploadInvalidException;
import dockerregistry.internal.error.exception.BlobUploadUnkownException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class BlobService {

    private static final Logger logger = LoggerFactory.getLogger(BlobService.class);

    private Path path;

    @Inject
    BlobRepository blobRepository;

    @PostConstruct
    protected void createRegistryDirectory() throws IOException {
        path = Files.createTempDirectory("tmpRegistry");
    }

    @Transactional
    public String getUniqueId(String name, String digest) {
        var blob = new BlobEntity();
        blob.setCreatedAt(LocalDateTime.now());
        blob.setName(name);
        blob.setDigest(digest);

        blobRepository.persist(blob);

        return Long.toString(blob.getId());
    }

    public long getLayerSize(String digest) {
        var hash = digest.split(":")[1];
        try {
            return Files.size(path.resolve(hash));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean layerExists(String name, String digest) {
        logger.debug("Check if image {} with digest {} is present", name, digest);

        var hash = digest.split(":")[1];

        return blobRepository.findByDigest(hash).isPresent();
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
     * @param uuid
     * @param digest
     */
    @Transactional
    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];
        var target = path.resolve(hash).toFile();

        var blob = blobRepository.findByIdOptional(Long.parseLong(uuid))
                .orElseThrow(BlobUploadUnkownException::new);

        blob.setDigest(hash);

        logger.debug("Rename layer {} to {}", uuid, hash);

        path.resolve(uuid).toFile().renameTo(target);
    }

    @Transactional
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
