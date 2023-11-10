package dockerregistry.extension.storage.filesystem;

import dockerregistry.extension.storage.Storage;
import dockerregistry.extension.storage.UnknownException;
import dockerregistry.extension.storage.UploadInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class FileSystemStorage implements Storage {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorage.class);

    private Path root;

    public FileSystemStorage(Path root) {
        this.root = root;
    }

    @Override
    public long upload(String range, String uuid, InputStream inputStream) {
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
            throw new UploadInvalidException(e);
        }

    }

    @Override
    public byte[] download(String name, String digest) {
        logger.debug("Get layer {} for image {}", digest, name);

        var hash = digest.split(":")[1];
        var target =  root.resolve(hash);

        try {
            logger.debug("Read file {}", target);

            return Files.readAllBytes(target);

        } catch (IOException e) {
            throw new UnknownException(e);
        }
    }

    @Override
    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];
        var target = root.resolve(hash).toFile();

        logger.debug("Rename layer {} to {}", uuid, hash);

        root.resolve(uuid).toFile().renameTo(target);
    }

    @Override
    public long getLayerSize(String hash) {
        try {
            return Files.size(root.resolve(hash));
        } catch (IOException e) {
            throw new UnknownException(e);
        }
    }
}
