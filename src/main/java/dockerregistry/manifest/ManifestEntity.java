package dockerregistry.manifest;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "manifests")
@NamedQuery(
        name = "ManifestEntity.findByDigest",
        query = "select m from ManifestEntity as m where digest = :digest"
)
@NamedQuery(
        name = "ManifestEntity.findByTagAndName",
        query = "select m from ManifestEntity as m where tag = :tag and name = :name"
)
public class ManifestEntity {

    @Id
    @SequenceGenerator(name = "manifest_gen", sequenceName = "manifest_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manifest_gen")
    private long id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String digest;

    private String tag;

    private long length;

    @Column(name = "media_type")
    private String mediaType;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private byte[] content;

    public long getId() {
        return id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
