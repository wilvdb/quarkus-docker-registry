package dockerregistry.blob;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blobs")
@NamedQuery(
        name = "BlobEntity.findByDigest",
        query = "select b from BlobEntity as b where b.digest = :digest"
)
@NamedQuery(
        name = "BlobEntity.findByUuid",
        query = "select b from BlobEntity as b where b.uuid = :uuid"
)
public class BlobEntity {

    @Id
    @SequenceGenerator(name = "blob_gen", sequenceName = "blob_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blob_gen")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String digest;

    @Column(nullable = false)
    private String name;

    private long length;

    @Column(nullable = false)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
