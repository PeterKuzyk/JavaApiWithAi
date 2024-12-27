package recruit;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataDrivenTest {

    private static List<String> candidateIds = new ArrayList<>();
    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1";
    }

    @Test
    @Order(1)
    public void testLogin() {
        Map<String, String> loginCredentials = new HashMap<>();
        loginCredentials.put("email", "student@example.com");
        loginCredentials.put("password", "welcome");

        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(loginCredentials)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().response();

        token = loginResponse.path("token");
    }

    private static Stream<Map<String, Object>> candidateProvider() {
        return Stream.of(
                createCandidate("TestName1", "TestLastname1", "12345Abc", "CA", "10000"),
                createCandidate("TestName2", "TestLastname2", "54321Cba", "NY", "20000"),
                createCandidate("TestName3", "TestLastname3", "67890Def", "TX", "30000")
        );
    }

    private static Map<String, Object> createCandidate(String firstName, String lastName, String password, String state, String zip) {
        Random random = new Random();
        int randomValue = 1000 + random.nextInt(9000);
        String email = "candidate" + randomValue + "@portnov.com";

        Map<String, Object> candidate = new HashMap<>();
        candidate.put("firstName", firstName);
        candidate.put("middleName", "");
        candidate.put("lastName", lastName);
        candidate.put("email", email);
        candidate.put("password", password);
        candidate.put("address", "");
        candidate.put("city", "");
        candidate.put("state", state);
        candidate.put("zip", zip);
        candidate.put("summary", "");

        return candidate;
    }

    @ParameterizedTest
    @MethodSource("candidateProvider")
    @Order(2)
    public void testCreateCandidate(Map<String, Object> candidate) {
        Response createCandidateResponse = given()
                .contentType(ContentType.JSON)
                .body(candidate)
                .when()
                .post("/candidates")
                .then()
                .statusCode(201)
                .extract().response();

        String candidateId = createCandidateResponse.path("id").toString();
        candidateIds.add(candidateId);
    }

    @Test
    @Order(3)
    public void testDeleteCandidates() {
        for (String candidateId : candidateIds) {
            given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/candidates/" + candidateId)
                    .then()
                    .statusCode(204);
        }
    }
}