package dockerregistry.manifest;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Table(name = "manifests")
public class ManifestEntity {

    @Id
    @SequenceGenerator(name = "manifest_gen", sequenceName = "manifest_seq")
    @Column(nullable = false)
    private long id;



    public long getId() {
        return id;
    }


}
