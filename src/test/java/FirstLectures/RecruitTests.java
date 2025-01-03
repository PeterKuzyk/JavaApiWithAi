package FirstLectures;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;

import static io.restassured.RestAssured.given;

public class RecruitTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1/";
    }

    @Test
    public void testLogin() {
        JsonObject credentials = Json.createObjectBuilder()
                .add("email","student@example.com")
                .add("password", "welcome")
                .build();

        given()
                .log().all()
                .contentType("application/json")
                .body(credentials.toString())
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200);
    }
}
