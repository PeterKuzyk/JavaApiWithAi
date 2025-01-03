package FirstLectures;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskBoardTests {

    private static int taskId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://taskboard.portnov.com";
    }

    @Test
    @Order(1)
    public void testGetAllTasks() {
        given()
                .log().all()
                .when()
                .get("/api/Task")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void createTask() throws IOException {
        String createRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/createRequestBody.json")));

        Response response = given()
                // .header("Content-Type", "application/json")
                .contentType("application/json")
                .body(createRequestBody)
                .log().all()
                .when()
                .post("/api/Task")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();

        taskId = response.jsonPath().getInt("id");
    }

    @Test
    @Order(3)
    public void updateTaskById() throws IOException {

        String updateRequestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/updateRequestBody.json")));
        updateRequestBody = updateRequestBody.replace("{id}", String.valueOf(taskId));

      given()
                .header("Content-Type", "application/json")
                .body(updateRequestBody)
                .log().all()
                .when()
                .put("/api/Task/" + taskId)  // Ensure the URL uses the correct taskId
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    @Order(4)
    public void getTaskById() {

        given()
                .log().all()
                .when()
                .get("/api/Task/" + taskId)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(5)
    public void deleteTaskById() {
        System.out.println("Response ID: " + taskId);
        given()
                .log().all()
                .when()
                .delete("/api/Task/" + taskId)
                .then()
                .statusCode(204);
    }
}