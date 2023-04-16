package dockerregistry.internal.rest;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Objects;

public class ResponseBuilder {

    public static final String DOCKER_API_VERSION_HEADER = "Docker-Distribution-API-Version";
    public static final String DOCKER_API_VERSION = "dockerregistry/2.0";
    private Response.ResponseBuilder builder;

    private ResponseBuilder() {

    }

    public static ResponseBuilder ok(Object entity) {
        var self = new ResponseBuilder();

        self.builder = Response.ok(Objects.requireNonNull(entity));
        return self;
    }

    public static ResponseBuilder ok() {
        var self = new ResponseBuilder();

        self.builder = Response.ok();
        return self;
    }


    public static ResponseBuilder notFound() {
        var self = new ResponseBuilder();

        self.builder = Response.status(Response.Status.NOT_FOUND);
        return self;
    }

    public static ResponseBuilder accepted() {
        var self = new ResponseBuilder();

        self.builder = Response.accepted();
        return self;
    }

    public static ResponseBuilder created(String uri) {
        var self = new ResponseBuilder();

        self.builder = Response.created(URI.create(Objects.requireNonNull(uri)));
        return self;
    }

    public ResponseBuilder dockerContentDigest(String digest) {
        builder.header("Docker-Content-Digest", Objects.requireNonNull(digest));

        return this;
    }

    public ResponseBuilder dockerUploadUuid(String uuid) {
        builder.header("Docker-Upload-Uuid", Objects.requireNonNull(uuid));

        return this;
    }

    public ResponseBuilder contentLength(long length) {
        builder.header("Content-Length", Objects.requireNonNull(length));

        return this;
    }

    public ResponseBuilder location(String location) {
        builder.header("location", Objects.requireNonNull(location));

        return this;
    }

    public ResponseBuilder contentType(String mediaType) {
        builder.header("Content-Type", Objects.requireNonNull(mediaType));

        return this;
    }

    public ResponseBuilder dockerDistributionApiVersion() {
        builder.header(DOCKER_API_VERSION_HEADER, DOCKER_API_VERSION);

        return this;
    }

    public ResponseBuilder range(long start, long end) {
        if(start < 0) {
            throw new IllegalArgumentException("Start range cannot be negative");
        }

        if(end < 0) {
            throw new IllegalArgumentException("End range cannot be negative");
        }

        if(start > end) {
            throw new IllegalArgumentException("Start should be upper than end");
        }

        builder.header("range", start + "-" + end);

        return this;
    }

    public ResponseBuilder range(long end) {
        return range(0, end);
    }

    public Response build() {
        return builder.build();
    }
}
