package dockerregistry.manifest;

import dockerregistry.internal.digest.DigestService;
import dockerregistry.internal.error.exception.ManifestBlobUnknownException;
import dockerregistry.internal.error.exception.ManifestInvalidException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    @Inject
    @Named("sha256DigestService")
    DigestService digestService;

    @Inject
    ManifestRepository repository;

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

        try {
            byte[] content = input.readAllBytes();

            var manifest = new ManifestEntity();
            manifest.setCreatedAt(LocalDateTime.now());
            manifest.setName(name);
            manifest.setTag(reference);
            manifest.setDigest(digestService.getDigest(content));
            manifest.setLength(content.length);
            manifest.setMediaType(getMediaType(content));
            manifest.setContent(content);
            repository.persist(manifest);

            return new Manifest(manifest.getDigest(), manifest.getName(), manifest.getTag(), manifest.getLength(), manifest.getMediaType());
        } catch (IOException e) {
            throw new ManifestBlobUnknownException(e);
        }
    }

    private String getMediaType(byte[] content) {
        try (var inputStream = new ByteArrayInputStream(content); var jsonReader = Json.createReader(inputStream)) {
            var mediaType = jsonReader.readObject().getString("mediaType");
            logger.debug("Manifest media type is {}", mediaType);

            return mediaType;

        } catch (IOException e) {
            throw new ManifestInvalidException(e);
        }
    }

}
