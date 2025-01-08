package image_ai;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class AnalyzeFacesTest {

    private static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Image";
    }

    @Test
    public void testAnalyzeFaces() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/sad-man.jpg");
        byte[] imageBytes = IOUtils.toByteArray(inputStream);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("base64Image", base64Image);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .body(requestBody)
                .when()
                .post("/analyze-faces")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> faces = response.path("");
        boolean foundSadEmotion = false;

        for (Map<String, Object> face : faces) {
            List<Map<String, Object>> emotions = (List<Map<String, Object>>) face.get("emotions");
            for (Map<String, Object> emotion : emotions) {
                Map<String, String> type = (Map<String, String>) emotion.get("type");
                if ("SAD".equals(type.get("value"))) {
                    Number confidence = (Number) emotion.get("confidence");
                    assertThat(confidence.doubleValue(), greaterThanOrEqualTo(90.0));
                    foundSadEmotion = true;
                    break;
                }
            }
            if (foundSadEmotion) {
                break;
            }
        }

        assertThat("SAD emotion not found", foundSadEmotion);
    }

    @Test
    public void testAnalyzeFaces2() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/happy-woman.jpg");
        byte[] imageBytes = IOUtils.toByteArray(inputStream);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("base64Image", base64Image);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .body(requestBody)
                .when()
                .post("/analyze-faces")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> faces = response.path("");
        boolean foundSadEmotion = false;

        for (Map<String, Object> face : faces) {
            List<Map<String, Object>> emotions = (List<Map<String, Object>>) face.get("emotions");
            for (Map<String, Object> emotion : emotions) {
                Map<String, String> type = (Map<String, String>) emotion.get("type");
                if ("HAPPY".equals(type.get("value"))) {
                    Number confidence = (Number) emotion.get("confidence");
                    assertThat(confidence.doubleValue(), greaterThanOrEqualTo(90.0));
                    foundSadEmotion = true;
                    break;
                }
            }
            if (foundSadEmotion) {
                break;
            }
        }

        assertThat("HAPPY emotion not found", foundSadEmotion);
    }
}
