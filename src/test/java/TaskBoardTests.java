import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TaskBoardTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://taskboard.portnov.com";
    }

    @Test
    public void testGetAllTasks() {
    given()
            .log().all()
            .when()
            .get("/api/Task")
            .then()
            .log().all()
            .statusCode(200);
    }



}
