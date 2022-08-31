package Fruit;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;

@QuarkusTest
@TestHTTPEndpoint(FruitResource.class)
@Slf4j
public class FruitResourceTest {

    @Test
    public void getAllFruits() {
        given()
                .when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", equalTo(3))
                .body("name", hasItems("Apple", "Orange", "Pear"))
                .contentType(ContentType.JSON);
    }

    @Test
    public void getFruitById() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        FruitDto got = given()
                .when()
                .get("/{id}", saved.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("name", Matchers.equalTo(saved.getName()))
                .extract().as(FruitDto.class);
        assertThat(got).isNotNull();
        assertThat(saved).isEqualTo(got);
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/{id}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void createFruit() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isNotNull();
    }

    @Test
    public void updateFruit() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        saved.setName("Banana");
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{id}", saved.getId())
                .then()
                .body("name", equalTo("Banana"))
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
        assertThat(saved).isNotNull();
    }

    @Test
    public void UpdateByIdNotEqualIdFruit() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{id}", 987654321)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void UpdateByIdNotExist() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        saved.setId(10L);
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{id}", 10)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void UpdateWithEmptyName() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        saved.setName(null);
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{id}", saved.getId())
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void deleteFruit() {
        FruitDto dto = createDto();
        FruitDto saved = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .extract().as(FruitDto.class);
        assertThat(saved).isNotNull();
        given()
                .when()
                .delete("/{id}", saved.getId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void deleteByIdNotFound() {
        given()
                .when()
                .get("/{id}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Create FruitDto for test
     *
     * @return FruitDto
     */
    private FruitDto createDto() {
        FruitDto dto = new FruitDto();
        dto.setId(Long.valueOf(RandomStringUtils.randomNumeric(1)));
        dto.setName(RandomStringUtils.randomAlphabetic(10));
        return dto;
    }
}
