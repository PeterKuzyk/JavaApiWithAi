package FirstLectures;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PracticeWithTeacher {

    private static int taskId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://taskboard.portnov.com";
    }

    @Test
    @Order(1)
    public void getAllTasks() {

        given()
                .when()
                .log().all()
                .get("/api/Task")
                .then()
                .log().all()
                .statusCode(200);

    }

    @Test
    @Order(2)
    public void createTask() {

        String body = "{\n" +
                "  \"id\": 0,\n" +
                "  \"taskName\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"dueDate\": \"2024-12-18T03:31:17.940Z\",\n" +
                "  \"priority\": 0,\n" +
                "  \"status\": \"string\",\n" +
                "  \"author\": \"string\"\n" +
                "}";
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(body)
                .when()
                .post("/api/Task")
                .then()
                .statusCode(201)
                .extract()
                .response();

        taskId = response.path("id");
        System.out.println(taskId);
    }

    @Test
    @Order(3)
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
    @Order(4)
    public void testUpdateTaskById() {

        String updatedIdBody = "{\n" +
                "  \"id\": " + taskId + ",\n" +
                "  \"taskName\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"dueDate\": \"2024-12-18T03:31:17.940Z\",\n" +
                "  \"priority\": 0,\n" +
                "  \"status\": \"string\",\n" +
                "  \"author\": \"string\"\n" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .body(updatedIdBody)
                .when()
                .put("/api/Task/" + taskId)
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testDeleteTaskById() {

        given()
                .when()
                .delete("/api/Task/" + taskId)
                .then()
                .statusCode(204);
    }
}