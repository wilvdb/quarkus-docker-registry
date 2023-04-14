package dockerregistry.internal.rest;

import dockerregistry.internal.config.RegistryConfiguration;
import dockerregistry.internal.error.exception.ErrorIdentifier;
import dockerregistry.internal.error.exception.TooManyRequestException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.matcher.RestAssuredMatchers;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TooManyRequestFilterTest {

    @Inject
    RegistryConfiguration configuration;

    @BeforeEach
    void set_up() {
        with().pollDelay(configuration.tooManyRequest().timestamp())
                .await().untilAsserted(() -> {
                    for(int i = 0; i < configuration.tooManyRequest().limit(); i++) {
                        given()
                                .when().get("/v2")
                                .then()
                                .statusCode(200);
                    }
                });

    }

    @Test
    void too_many_request() {
        given()
                .when().get("/v2")
                .then()
                .statusCode(400)
                .body("errors.size()", equalTo(1),
                        "errors[0].code", equalTo(ErrorIdentifier.TOO_MANY_REQUEST.getError().code()),
                        "errors[0].message", equalTo(ErrorIdentifier.TOO_MANY_REQUEST.getError().message()),
                        "errors[0].description", equalTo(ErrorIdentifier.TOO_MANY_REQUEST.getError().description()));
    }

    @Test
    void many_request_after_timestamp() throws InterruptedException {
        with().pollDelay(configuration.tooManyRequest().timestamp())
                .await().untilAsserted(() -> given()
                .when().get("/v2")
                .then()
                .statusCode(200));

    }
}
