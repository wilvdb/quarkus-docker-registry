package dockerregistry.manifest;

import dockerregistry.internal.digest.DigestService;
import dockerregistry.internal.error.exception.ManifestInvalidException;
import dockerregistry.internal.error.exception.NameUnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dockerregistry.internal.error.exception.ManifestBlobUnknownException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    private Path path;

    @Inject
    DigestService digestService;

    @Inject
    ManifestRepository repository;

    @PostConstruct
    protected void createRegistryDirectory() throws IOException {
        path = Files.createTempDirectory("tmpRegistry");
    }

    public boolean manifestExists(String name, String reference) {
        logger.debug("Check manifest for image {} and reference {}", name, reference);

        var hash = reference.split(":")[0];

        var tagPath = path.resolve(name + "." + reference + ".json").toFile();
        var hashPath = path.resolve(hash + ".json").toFile();
        return tagPath.exists() && hashPath.exists();

    }

    public String saveManifest(String name, String reference, InputStream input) {
        logger.debug("Save manifest for image {} and tag {}", name, reference);

        var tagPath = path.resolve(name + "." + reference + ".json");

        try (var outputChannel = Files.newByteChannel(tagPath, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            logger.debug("Write manifest to file {}", tagPath);
            outputChannel.write(ByteBuffer.wrap(input.readAllBytes()));

            var hash = getSha256(name, reference);
            var hashPath = path.resolve(hash + ".json");
            Files.copy(tagPath, hashPath);

            return hash;
        } catch (IOException e) {
            throw new ManifestBlobUnknownException(e);
        }
    }

    public String getSha256(String name, String reference) {
        logger.debug("Get manifest digest for image {} with reference {}", name, reference);

        try {
            var tagPath = path.resolve(name + "." + reference + ".json");

            return digestService.getDigest(Files.readAllBytes(tagPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public long getContentLength(String name, String reference) {
        logger.debug("Get manifest content size for image {} with reference {}", name, reference);

        try {
            return Files.size(path.resolve(name + "." + reference + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType(String name, String reference) {
        logger.debug("Get manifest media type for image {} with reference {}", name, reference);

        var manifestPath = getManifestPath(name, reference);
        try (var reader = Files.newBufferedReader(manifestPath); var jsonReader = Json.createReader(reader)) {

            var mediaType = jsonReader.readObject().getString("mediaType");
            logger.debug("Manifest media type is {}", mediaType);

            return mediaType;

        } catch (IOException e) {
            throw new ManifestInvalidException(e);
        }
    }

    public void checkExistence(String name, String reference) {
        logger.debug("Check existence manifest with reference {}", reference);

        if(getManifestPath(name, reference) == null) {
            throw new NameUnknownException();
        }
    }

    public byte[] getContent(String name, String reference) {
        logger.debug("Get manifest for image {} with reference {}", name, reference);

        try {
            return Files.readAllBytes(getManifestPath(name, reference));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getManifestPath(String name, String reference) {
        if(reference.contains(":")) {
            var hash = reference.split(":")[1];


            var hashPath = path.resolve(hash + ".json");
            return Files.exists(hashPath) ? hashPath : null;
        }
        var tagPath = path.resolve(name + "." + reference + ".json");
        return Files.exists(tagPath) ? tagPath : null;

    }

}
