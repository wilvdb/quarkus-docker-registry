package dockerregistry.manifest;

import dockerregistry.internal.digest.DigestService;
import dockerregistry.internal.error.exception.ManifestInvalidException;
import dockerregistry.internal.error.exception.NameUnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dockerregistry.internal.error.exception.ManifestBlobUnknownException;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Transactional
    public Optional<Manifest> getManifest(String name, String reference) {
        logger.debug("Check manifest for image {} and reference {}", name, reference);

        var hash = reference.split(":")[0];

        return repository.findByDigest(hash)
                .or(() -> repository.findByTagAndName(name, reference))
                .map(entity -> new Manifest(entity.getDigest(), entity.getName(), entity.getTag(), entity.getLength(), entity.getMediaType()));

    }

    @Transactional
    public Manifest saveManifest(String name, String reference, InputStream input) {
        logger.debug("Save manifest for image {} and tag {}", name, reference);

        var tagPath = path.resolve(name + "." + reference + ".json");


        try (var outputChannel = Files.newByteChannel(tagPath, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            logger.debug("Write manifest to file {}", tagPath);
            byte[] content = input.readAllBytes();
            outputChannel.write(ByteBuffer.wrap(content));

            var manifest = new ManifestEntity();
            manifest.setCreatedAt(LocalDateTime.now());
            manifest.setName(name);
            manifest.setTag(reference);
            manifest.setDigest(digestService.getDigest(content));
            manifest.setLength(Files.size(tagPath));
            manifest.setMediaType(getMediaType(name, reference));
            manifest.setContent(content);
            repository.persist(manifest);

            return new Manifest(manifest.getDigest(), manifest.getName(), manifest.getTag(), manifest.getLength(), manifest.getMediaType());
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
