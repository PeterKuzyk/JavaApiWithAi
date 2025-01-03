import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTestWithAiGoogleTranslateTest {

    private static final String API_KEY = System.getenv("KEY");

    // private static final String API_KEY = "AIzaSyANriv7yszmw-sbe8YN1GAmZDE5WYDDq1k";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://translation.googleapis.com/language/translate/v2";
    }

    @Test
    public void testTranslateText() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("q", "Hello, world!");
        requestBody.put("source", "en");
        requestBody.put("target", "es");
        requestBody.put("format", "text");


        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .body(requestBody)
                .when()
                .log().all()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String translatedText = response.path("data.translations[0].translatedText");
        assertEquals("¡Hola Mundo!", translatedText);
    }
}