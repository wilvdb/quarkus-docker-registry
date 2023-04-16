package dockerregistry.base;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static dockerregistry.internal.rest.ResponseBuilder.DOCKER_API_VERSION;
import static dockerregistry.internal.rest.ResponseBuilder.DOCKER_API_VERSION_HEADER;
import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(BaseResource.class)
class BaseResourceTest {

    @Test
    void base() {
        given().get()
                .then()
                .statusCode(200)
                .header(DOCKER_API_VERSION_HEADER, DOCKER_API_VERSION);
    }
}
