package dockerregistry.blob;

public record Blob(String uuid, String name, String digest, Long length, byte[] content) {

    public static BlobBuilder from(Blob blob) {
        return BlobBuilder.from(blob);
    }

    public static class BlobBuilder {

        private String uuid;

        private String name;

        private String digest;

        private Long length;

        private byte [] content;

        private BlobBuilder() {

        }
        public static BlobBuilder from(Blob blob) {
            var builder = new BlobBuilder();
            builder.content = blob.content();
            builder.length = blob.length();
            builder.digest = blob.digest();
            builder.name = blob.name();
            builder.uuid = blob.uuid();

            return builder;
        }

        public BlobBuilder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public BlobBuilder withLength(Long length) {
            this.length = length;
            return this;
        }

        public BlobBuilder withDigest(String digest) {
            this.digest = digest;
            return this;
        }

        public BlobBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public BlobBuilder withUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Blob build() {
            return new Blob(this.uuid, this.name, this.digest, this.length, this.content);
        }
    }
}
