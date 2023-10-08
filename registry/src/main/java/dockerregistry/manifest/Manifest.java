package dockerregistry.manifest;

public record Manifest(String digest, String name, String tag, long length, String mediaType) {
}
