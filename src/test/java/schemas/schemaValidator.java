package schemas;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class schemaValidator {

    private static String token;

    private static Map<String, String> env;

    @BeforeAll
    public static void setup() throws IOException{
        env = readJsonFromFile("src/test/resources/environtment.json");
        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1";
    }

    private static Map<String, String> readJsonFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new com.fasterxml.jackson.databind.ObjectMapper().readValue(content, Map.class);
    }

    private static void saveJsonToFile(String filePath, Map<String, String> data) throws IOException {
        new com.fasterxml.jackson.databind.ObjectMapper().writeValue(Paths.get(filePath).toFile(), data);
    }

    private String readJsonFromFileString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    @Order(1)
    public void testSchemaValidator1() throws IOException {
        String credentials = readJsonFromFileString("src/test/resources/studentLogin.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("data/authResponseSchema.json"))
                .extract().response();

        assertEquals(200, response.getStatusCode());
        token = response.path("token");
        System.out.println("Token: " + token);

        int issuedAt = response.path("issuedAt");
        int expiresAt = response.path("expiresAt");
        int result = expiresAt - issuedAt;
        Assertions.assertEquals(86400, result);
        System.out.println("Result: " + result);

        env.put("token", response.path("token"));
        saveJsonToFile("src/test/resources/environtment.json", env);

    }

    @Test
    @Order(2)
    public void testSchemaValidator2() throws IOException {
        String credentials = readJsonFromFileString("src/test/resources/studentLogin.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + env.get("token"))
                .body(credentials)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("data/authResponseSchema.json"))
                .extract().response();

        assertEquals(200, response.getStatusCode());
        System.out.println("Token: " + env.get("token"));

    }
}