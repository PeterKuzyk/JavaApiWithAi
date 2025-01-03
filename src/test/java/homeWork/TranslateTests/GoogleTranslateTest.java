package homeWork.TranslateTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GoogleTranslateTest {

    public static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://translation.googleapis.com/language/translate/v2";
    }

    @Test
    public void testTranslateToSpanish() throws IOException {

        String translateToSpanishBody = new String(Files.readAllBytes(Paths.get("src/test/resources/translateData/googleTranslateToSpanishBody.json")));

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .body(translateToSpanishBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String translatedText = response.path("data.translations[0].translatedText");
        assertThat(translatedText, anyOf(
                containsStringIgnoringCase("Hola Mundo"),
                containsStringIgnoringCase("¡")
        ));
    }

    @Test
    public void testTranslateToUkrainian() throws IOException {

        String translateToUkrainianBody = new String(Files.readAllBytes(Paths.get("src/test/resources/translateData/googleTranslatToUkrainianBody.json")));

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .body(translateToUkrainianBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String translatedToUkrainianText = response.path("data.translations[0].translatedText");
        assertThat(translatedToUkrainianText, anyOf(
                containsStringIgnoringCase("Тестування API - це цікаво!")
        ));
    }

    @Test
    public void testTranslateTosPolish() throws IOException {

        String translateToPolishBody = new String(Files.readAllBytes(Paths.get("src/test/resources/translateData/googleTranslateToPolishBody.json")));

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .body(translateToPolishBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String translatedToPlText = response.path("data.translations[0].translatedText");
        assertThat(translatedToPlText, anyOf(
                containsStringIgnoringCase("Testowanie API jest ciekawe!"),
                containsStringIgnoringCase("jest ciekawe")
        ));
    }

    @Test
    public void testTranslateToJapanese() throws IOException {

        String translateToJapaneseBody = new String(Files.readAllBytes(Paths.get("src/test/resources/translateData/googleTranslateToJapaneseBody.json")));

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", API_KEY)
                .body(translateToJapaneseBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String translatedToJapaneseText = response.path("data.translations[0].translatedText");
        assertThat(translatedToJapaneseText, anyOf(
                containsStringIgnoringCase("APIテストは面白いですね")
        ));
    }
}