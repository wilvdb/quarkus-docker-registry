package dockerregistry.internal.rest;

import dockerregistry.internal.config.RegistryConfiguration;
import dockerregistry.internal.error.exception.TooManyRequestException;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
public class TooManyRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TooManyRequestFilter.class);

    @Inject
    RegistryConfiguration configuration;

    private Map<String, Hits> ipCounter = new ConcurrentHashMap<>();

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        cleanupHits();

        var address = request.remoteAddress().hostAddress();
        ipCounter.putIfAbsent(address, new Hits(configuration.tooManyRequest().timestamp()));
        var hits = ipCounter.computeIfPresent(address, (s, value) -> {
            value.addHit();
            return value;
        });

        var count = hits.countHits();
        if(count > configuration.tooManyRequest().limit()) {
            logger.warn(String.format("Client %s exceed request limit %d", address, count));
            throw new TooManyRequestException();
        }

    }

    private void cleanupHits() {
        ipCounter.forEach((ip, hits) -> hits.cleanup());

        ipCounter.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    static class Hits {

        private final Duration timestamp;

        public Hits(Duration timestamp) {
            this.timestamp = timestamp;
        }

        private final List<Instant> hits = new ArrayList<>();

        public void addHit() {
            hits.add(Instant.now());
        }

        public void cleanup() {
            hits.removeIf(this::isAfter);
        }

        public boolean isEmpty() {
            return countHits() == 0;
        }

        private boolean isAfter(Instant hit) {
            return Instant.now().minus(timestamp).isAfter(hit);
        }

        public int countHits() {
            return hits.size();
        }

    }

}
