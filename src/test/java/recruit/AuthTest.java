package recruit;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1";
    }

    private String readJsonFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    @Order(1)
    public void testLogin() throws IOException {
        String credentials = readJsonFromFile("src/test/resources/studentLogin.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals(200, response.getStatusCode());
        token = response.path("token");
        System.out.println("Token: " + token);
    }

    @Test
    @Order(2)
    public void testVerifyRequestWithToken() {
        given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")))
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/verify")
                .then()
                .log().all()
                .statusCode(200);
    }
}

