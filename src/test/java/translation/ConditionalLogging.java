package translation;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class ConditionalLogging {

    private static final String API_KEY = System.getenv("KEY");;
    private static final boolean ENABLE_LOGGING = Boolean.parseBoolean(System.getenv("ENABLE_LOGGING"));

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://translation.googleapis.com/language/translate/v2";
    }

    private List<Filter> getFilters() {
        List<Filter> filters = new ArrayList<>();
        if (ENABLE_LOGGING) {
            filters.add(new RequestLoggingFilter());
            filters.add(new ResponseLoggingFilter());
        }
        return filters;
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
                .filters(getFilters())
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        String translatedText = response.path("data.translations[0].translatedText");
        assertThat(translatedText.replace(",", ""), containsStringIgnoringCase("Hola mundo"));
    }

    @Test
    public void testTranslateUncertainText() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("q", "bank");
        requestBody.put("source", "en");
        requestBody.put("target", "es");
        requestBody.put("format", "text");

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .filters(getFilters())
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        String translatedText = response.path("data.translations[0].translatedText");
        assertThat(translatedText, anyOf(
                containsStringIgnoringCase("banco"),
                containsStringIgnoringCase("orilla")
        ));
    }
}