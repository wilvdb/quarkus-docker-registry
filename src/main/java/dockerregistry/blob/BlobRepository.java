package dockerregistry.blob;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class BlobRepository implements PanacheRepository<BlobEntity> {

    public Optional<BlobEntity> findByDigest(String digest) {
        return find("#BlobEntity.findByDigest", Parameters.with("digest", digest)).singleResultOptional();
    }
}
