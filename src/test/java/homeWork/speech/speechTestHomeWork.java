package homeWork.speech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class speechTestHomeWork {

    public static final String API_KEY = System.getenv("KEY");

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Speech";
    }

    @Test
    public void testConversationToText() {
        File myAudio = new File("myTestfFle.mp3");

        Response response = given()
                .contentType(ContentType.MULTIPART)
                .header("X-Api-Key", API_KEY)
                .multiPart("audioFile", myAudio)
                .when()
                .post("/convert-speech-to-text")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String transcript = response.path("jsonResponse.results.transcripts[0].transcript");
        assertThat(transcript, equalTo ("Hey, this is my API test recording 123. Kajalarevet."));
    }

    @Test
    public void testConversationToText2() {
        File myAudioTwo = new File("file2.mp3");

        Response response = given()
                .contentType(ContentType.MULTIPART)
                .header("X-Api-Key", API_KEY)
                .multiPart("audioFile", myAudioTwo)
                .when()
                .post("/convert-speech-to-text")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String transcript = response.path("jsonResponse.results.transcripts[0].transcript");
        assertThat(transcript, equalTo("Credit vapor."));
    }

    @Test
    public void testConversationToText3() {
        File myAudioTwo = new File("file2.mp3");

        Response response = given()
                .contentType(ContentType.MULTIPART)
                .header("X-Api-Key", API_KEY)
                .multiPart("audioFile", myAudioTwo)
                .when()
                .post("/convert-speech-to-text")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String transcript = response.path("jsonResponse.results.transcripts[0].transcript");
        assertThat(transcript, equalTo("Credit vapor."));
    }
}
