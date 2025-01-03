package homeWork.recruitPractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTest {

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1";
    }

    private String readJasonFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    public void testLogin() throws IOException {

        String loginCred = readJasonFromFile("src/test/resources/studentLogin.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginCred)
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        boolean loggedInData = response.jsonPath().getBoolean("authenticated");
        assertTrue(loggedInData, "Expected 'authenticated' to be true");
    }
}