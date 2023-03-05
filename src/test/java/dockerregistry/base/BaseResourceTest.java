package dockerregistry.base;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static dockerregistry.base.BaseResource.DOCKER_REGISTRY_VERSION;
import static dockerregistry.base.BaseResource.DOCKER_VERSION_HEADER;
import static io.restassured.RestAssured.given;

@QuarkusTest
class BaseResourceTest {

    @Test
    void base() {
        given().get("/v2")
                .then()
                .statusCode(200)
                .header(DOCKER_VERSION_HEADER, DOCKER_REGISTRY_VERSION);
    }
}
