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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Provider
public class TooManyRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TooManyRequestFilter.class);

    private static long TIMESTAMP = 5000l;

    static int LIMIT = 100;

    private Map<String, Hits> ipCounter = new HashMap<>();

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        ipCounter.putIfAbsent(request.remoteAddress().hostAddress(), new Hits());
        var hits = ipCounter.computeIfPresent(request.remoteAddress().hostAddress(), (s, value) -> {
            value.addHit();
            return value;
        });

        if(hits.countHits() > LIMIT) {
            throw new TooManyRequestException();
        }

    }

    static class Hits {

        private List<Instant> hits = new ArrayList<>();

        public void addHit() {
            var now = Instant.now();
            hits.add(now);

            hits.removeIf(hit -> isAfter(now, hit));
        }

        private boolean isAfter(Instant now, Instant hit) {
            return now.minus(Duration.of(TIMESTAMP, ChronoUnit.MILLIS)).isAfter(hit);
        }

        public int countHits() {
            return hits.size();
        }

    }

}
