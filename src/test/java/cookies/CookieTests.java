package cookies;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CookieTests {

    @Test
    void testGetAndCheckCookie() {
        // Set the base URI
        RestAssured.baseURI = "https://image-ai.portnov.com/api/Cookie";

        // Get cookies from the first endpoint
        Response response = given()
                .when()
                .get("/get")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Search for the cookie key "test"
        String testCookie = response.getCookie("test");
        assertNotNull(testCookie, "The 'test' cookie should not be null");

        // Pass the cookie to the second endpoint
        given()
                .cookie("test", testCookie)
                .when()
                .get("/check")
                .then()
                .log().all()
                .statusCode(200);
    }
}
