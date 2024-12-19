import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PracticeWithTeacher {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI =  "https://taskboard.portnov.com";
    }

    @Test
    public void createTack() {

        String body = "{\n" +
                "  \"id\": 0,\n" +
                "  \"taskName\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"dueDate\": \"2024-12-18T03:31:17.940Z\",\n" +
                "  \"priority\": 0,\n" +
                "  \"status\": \"string\",\n" +
                "  \"author\": \"string\"\n" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(body)
                .when()
                .post("/api/Task")
                .then()
                .statusCode(201);
    }

}