package dockerregistry.manifest;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import static io.quarkus.panache.common.Parameters.with;

@ApplicationScoped
public class ManifestRepository implements PanacheRepository<ManifestEntity> {

    public Optional<ManifestEntity> findByDigest(String digest) {
        return find("#ManifestEntity.findByDigest", with("digest", digest)).
                firstResultOptional();
    }

    public Optional<ManifestEntity> findByTagAndName(String name, String tag) {
        return find("#ManifestEntity.findByTagAndName", with("tag", tag).and("name", name)).
                firstResultOptional();
    }
}
