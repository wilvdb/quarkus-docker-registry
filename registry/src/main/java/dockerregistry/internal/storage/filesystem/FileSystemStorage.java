package dockerregistry.internal.storage.filesystem;

import dockerregistry.internal.storage.Storage;
import dockerregistry.internal.storage.UnknownException;
import dockerregistry.internal.storage.UploadInvalidException;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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

    private final Path location;

    public FileSystemStorage(Path location) {
        this.location = location;
    }

    @Override
    public long upload(String range, String uuid, InputStream inputStream) {
        Log.debugf("Upload blob from range %s UUID %s", range, uuid);

        var start = Optional.ofNullable(range)
                .map(r -> r.split("-")[0])
                .map(Long::parseLong)
                .orElse(0l);

        var filePath = location.resolve(uuid);

        try (var outputChannel = Files.newByteChannel(filePath, StandardOpenOption.APPEND, StandardOpenOption.CREATE).position(start)) {
            Log.debugf("Write to file %s", filePath);
            outputChannel.write(ByteBuffer.wrap(inputStream.readAllBytes()));

            Log.debug("finish upload the layer");

            return outputChannel.position();

        } catch (IOException e) {
            throw new UploadInvalidException(e);
        }

    }

    @Override
    public byte[] download(String name, String digest) {
        Log.debugf("Get layer %s for image %s", digest, name);

        var hash = digest.split(":")[1];
        var target =  location.resolve(hash);

        try {
            Log.debugf("Read file %s", target);

            return Files.readAllBytes(target);

        } catch (IOException e) {
            throw new UnknownException(e);
        }
    }

    @Override
    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];
        var target = location.resolve(hash).toFile();

        Log.debugf("Rename layer %s to %s", uuid, hash);

        location.resolve(uuid).toFile().renameTo(target);
    }

    @Override
    public long getLayerSize(String hash) {
        try {
            return Files.size(location.resolve(hash));
        } catch (IOException e) {
            throw new UnknownException(e);
        }
    }
}
