package dockerregistry.internal.rest;

import dockerregistry.internal.error.exception.TooManyRequestException;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Provider
public class TooManyRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TooManyRequestFilter.class);

    static long TIMESTAMP = 5000l;

    static int LIMIT = 100;

    private Map<String, Hits> ipCounter = new ConcurrentHashMap<>();

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        var address = request.remoteAddress().hostAddress();
        ipCounter.putIfAbsent(address, new Hits());
        var hits = ipCounter.computeIfPresent(address, (s, value) -> {
            value.addHit();
            return value;
        });

        var count = hits.countHits();
        if(count > LIMIT) {
            logger.warn(String.format("Client %s exceed request limit %d", address, count));
            throw new TooManyRequestException();
        }

    }

    static class Hits {

        private final List<Instant> hits = new ArrayList<>();

        public void addHit() {
            hits.add(Instant.now());
        }

        private boolean isAfter(Instant hit) {
            return Instant.now().minus(Duration.of(TIMESTAMP, ChronoUnit.MILLIS)).isAfter(hit);
        }

        public int countHits() {
            hits.removeIf(this::isAfter);

            return hits.size();
        }

    }

}
