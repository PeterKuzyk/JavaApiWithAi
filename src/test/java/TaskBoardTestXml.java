import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;

import static io.restassured.RestAssured.given;

public class TaskBoardTestXml {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://taskboard.portnov.com";
    }

    @Test
    public void testGetAllTasksXml() {
        given()
                .log().all()
                .when()
                .get("/api/TaskXml")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void testCreateTaskXml() {

        String taskBody = """
                <?xml version="1.0" encoding="UTF-8"?>
                <TaskItem>
                  <id>0</id>
                  <taskName>PeterTest</taskName>
                  <description>Test</description>
                  <dueDate>2024-12-18T02:22:28.813Z</dueDate>
                  <priority>1</priority>
                  <status>Create</status>
                  <author>PK</author>
                </TaskItem>
                """;

        JsonObject credentials = Json.createObjectBuilder()
                .add("email","student@example.com")
                .add("password", "welcome")
                .build();
        given()
                .contentType(ContentType.XML)
                .log().all()
                .body(taskBody)
                .when()
                .post("/api/TaskXml")
                .then()
                .log().all()
                .statusCode(201);
    }
}