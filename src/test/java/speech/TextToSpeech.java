package speech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class TextToSpeech {
    private static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Speech";
    }

    @Test
    public void testConvertTextToSpeech() throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("text", "Hello, this is a test for text to speech conversion.");
        requestBody.put("voice", "Joanna");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .body(requestBody)
                .when()
                .post("/convert-text-to-speech")
                .then()
                .statusCode(200)
                .extract().response();

        byte[] mp3Bytes = response.asByteArray();
        try (FileOutputStream fos = new FileOutputStream("output.mp3")) {
            IOUtils.write(mp3Bytes, fos);
        }

        assertThat(mp3Bytes.length, greaterThan(0));
    }
}
