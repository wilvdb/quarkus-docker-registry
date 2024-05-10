package dockerregistry.blob;

import dockerregistry.internal.storage.Storage;
import dockerregistry.internal.error.exception.BlobUploadUnkownException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BlobService {

    private static final Logger logger = LoggerFactory.getLogger(BlobService.class);

    @Inject
    Storage storage;

    @Inject
    BlobRepository blobRepository;

    @Transactional
    public Blob createBlob(String name, String digest) {
        var blob = new BlobEntity();
        blob.setCreatedAt(LocalDateTime.now());
        blob.setName(name);
        blob.setDigest(digest);
        blob.setUuid(UUID.randomUUID().toString());

        blobRepository.persist(blob);

        return new Blob(blob.getUuid(), blob.getName(), blob.getDigest(), blob.getLength(), null);
    }



    @Transactional
    public Optional<Blob> layerExists(String name, String digest) {
        logger.debug("Check if image {} with digest {} is present", name, digest);

        var hash = digest.split(":")[1];

        return blobRepository.findByDigest(hash).
                map(entity -> new Blob(entity.getUuid(), entity.getName(), entity.getDigest(), entity.getLength(), null));
    }

    public long uploadLayer(String range, String uuid, InputStream inputStream) {
        return storage.upload(range, uuid, inputStream);

    }

    /**
     * @param uuid
     * @param digest
     */
    @Transactional
    public void finishUpload(String uuid, String digest) {
        var hash = digest.split(":")[1];

        var blob = blobRepository.findByUuid(uuid)
                .orElseThrow(BlobUploadUnkownException::new);

        blob.setDigest(hash);
        blob.setLength(storage.getLayerSize(uuid));

        logger.debug("Rename layer {} to {}", uuid, hash);

        storage.finishUpload(uuid, digest);
    }

    @Transactional
    public void finishUpload(String uuid, String digest, String range, InputStream body) {
        uploadLayer(range, uuid, body);

        finishUpload(uuid, digest);
    }

    @Transactional
    public Optional<Blob> getLayer(String name, String digest) {
        return layerExists(name, digest)
                .map(blob -> Blob.from(blob).withContent(storage.download(name, digest)).build());
    }

}
