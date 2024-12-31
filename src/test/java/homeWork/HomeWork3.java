package homeWork;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomeWork3 {

    private static String token;
    private static String candidateId;
    private static String applicationId;
    private static String email;

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://recruit-stage.portnov.com/recruit/api/v1";
    }

    private String readJasonFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    @Order(1)
    public void testLogin() throws IOException {

        String credentials = readJasonFromFile("src/test/resources/studentLogin.json");

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        assertEquals(200, response.getStatusCode());
        token = response.path("token");
        System.out.println("Token: " + token);

    }

    @Test
    @Order(2)
    public void testCreateCandidate() throws IOException {

        Random random = new Random();
        int randomValue = 1000 + random.nextInt(9000);
        email = "pk" + randomValue + "@portnov.com";

        String createCandidateBody = new String(Files.readAllBytes(Paths.get("src/test/resources/createCandidate.json")));
        createCandidateBody = createCandidateBody.replace("{email}", String.valueOf(email));

        Response createCandidateResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(createCandidateBody)
                .when()
                .post("/candidates")
                .then()
                .log().all()
                .statusCode(201)
                .extract().response();

        System.out.println(email);
        candidateId = createCandidateResponse.path("id").toString();
    }

    @Test
    @Order(3)
    public void testApplyToPosition() throws IOException {
        Map<String, Object> application = new HashMap<>();
        application.put("candidateId", candidateId);
        application.put("positionId", 1);
        application.put("dateApplied", LocalDate.now().toString());

        Response applyToPositionResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(application)
                .when()
                .post("/applications")
                .then()
                .log().all()
                .statusCode(201)
                .extract().response();

        applicationId = applyToPositionResponse.path("id").toString();
    }

    @Test
    @Order(4)
    public void testValidateApplication() {
        given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/applications/" + applicationId)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(5)
    public void updateCandidate() throws IOException {

        String updateCandidateBody = new String(Files.readAllBytes(Paths.get("src/test/resources/updateCanididate.json")));
        updateCandidateBody = updateCandidateBody.replace("{email}", String.valueOf(email));

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(updateCandidateBody)
                .when()
                .put("/candidates/" + candidateId)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Order(6)
    public void testDeleteApplication() {

        given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/applications/" + applicationId)
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    @Order(7)
    public void testDeleteCandidate() {

        given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/candidates/" + candidateId)
                .then()
                .log().all()
                .statusCode(204);
    }
}