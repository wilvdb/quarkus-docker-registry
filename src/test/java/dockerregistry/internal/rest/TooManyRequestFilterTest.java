package dockerregistry.internal.rest;

import dockerregistry.internal.error.exception.TooManyRequestException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TooManyRequestFilterTest {

    @Test
    void too_many_request() {
        for(int i = 0; i < TooManyRequestFilter.LIMIT; i++) {
            given()
                    .when().get("/v2")
                    .then()
                    .statusCode(200);
        }

        given()
                .when().get("/v2")
                .then()
                .statusCode(400)
                .body(is("test"));
    }
}
