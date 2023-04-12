package dockerregistry.manifest;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import static io.quarkus.panache.common.Parameters.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ManifestRepository implements PanacheRepository<ManifestEntity> {

    public Optional<ManifestEntity> findByDigest(String digest) {
        return find("select m from ManifestEntity as m where digest = :digest",
                with("digest", digest)
        ).stream()
                .findFirst();
    }

    public Optional<ManifestEntity> findByTagAndName(String name, String tag) {
        return find("select m from ManifestEntity as m where tag = :tag and name = :name",
                with("tag", tag).and("name", name)
        ).stream()
                .findFirst();
    }
}
