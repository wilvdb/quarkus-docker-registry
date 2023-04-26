package dockerregistry.blob;

public record Blob(String uuid, String name, String digest, Long length) {
}
