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
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class DetectLabelsTest {

    private static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Image";
    }

    @Test
    public void testDetectLabels() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/golden-gate.jpg");
        byte[] imageBytes = IOUtils.toByteArray(inputStream);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("base64Image", base64Image);
        requestBody.put("maxLabels", 10);
        requestBody.put("minConfidence", 70);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .body(requestBody)
                .when()
//                .log().all()
                .post("/detect-labels")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String responseBody = response.asString();
        assertThat(responseBody, containsString("Golden Gate Bridge"));
    }
}
