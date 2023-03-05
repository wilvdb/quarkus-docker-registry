package dockerregistry.internal.error.rest;

import dockerregistry.internal.error.exception.TooManyRequestException;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Provider
public class TooManyRequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TooManyRequestFilter.class);

    private Map<String, AtomicInteger> ipCounter = new HashMap<>();

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        ipCounter.putIfAbsent(request.remoteAddress().hostAddress(), new AtomicInteger());
        var counter = ipCounter.computeIfPresent(request.remoteAddress().hostAddress(), (s, value) -> {
            value.incrementAndGet();
            return value;
        });

        if(counter.get() > 100) {
            throw new TooManyRequestException();
        }
    }


}
