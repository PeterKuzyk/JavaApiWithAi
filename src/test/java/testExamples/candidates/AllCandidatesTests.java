package testExamples.candidates;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AllCandidatesTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1/";
    }

    @Test
    public void testGetAllCandidates() {

        given()
                .log().all()
                .when()
                .get("candidates")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAllPositions() {

        given()
                .log().all()
                .when()
                .get("positions")
                .then()
                .statusCode(200);
    }
}