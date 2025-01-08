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

public class CompareFacesTest {
    private static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Image";
    }

    @Test
    public void testCompareFaces() throws IOException {
        InputStream inputStream1 = getClass().getClassLoader().getResourceAsStream("images/arnie-1.jpg");
        byte[] imageBytes1 = IOUtils.toByteArray(inputStream1);

        InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("images/arnie-2.jpg");
        byte[] imageBytes2 = IOUtils.toByteArray(inputStream2);

        String base64Image1 = Base64.getEncoder().encodeToString(imageBytes1);
        String base64Image2 = Base64.getEncoder().encodeToString(imageBytes2);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sourceBase64Image", base64Image1);
        requestBody.put("targetBase64Image", base64Image2);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .body(requestBody)
                .when()
                .post("/compare-faces")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> faces = response.path("");
        for (Map<String, Object> face : faces) {
            Number similarity = (Number) face.get("similarity");
            assertThat(similarity.doubleValue(), greaterThanOrEqualTo(90.0));
        }
    }
}
