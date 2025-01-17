package json_parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentsTests {
    private static final Set<String> VALID_QUESTION_TYPES = Set.of("MULTIPLE_CHOICE", "SINGLE_CHOICE", "TEXTUAL");

    @Test
    void test1JsonFile() {
        JsonNode json = readJson("4.json");

        // Verify the number of objects in the JSON array
        assertEquals(7, json.size(), "The number of objects in the JSON array should be 4");

        // Validate the status field for each object
        for (JsonNode jsonObject : json) {
            String status = jsonObject.get("status").asText();
            assertEquals("ASSIGNED", status, "The status should be 'ASSIGNED'");

            // Validate the id field for each object
            int id = jsonObject.get("id").asInt();
            assertTrue(id > 0, "The id should be a positive integer");

            // Validate the date field for each object
            String createdAt = jsonObject.get("createdAt").asText();
            assertTrue(isValidDate(createdAt), "The createdAt should be a valid date");

            // Validate questions
            JsonNode questions = jsonObject.get("quiz").get("questions");
            for (JsonNode question : questions) {
                String type = question.get("type").asText();
                assertTrue(VALID_QUESTION_TYPES.contains(type), "The question type should be one of 'MULTIPLE_CHOICE', 'SINGLE_CHOICE', or 'TEXTUAL'");

                // Validate that the question has text
                String questionText = question.get("question").asText();
                assertNotNull(questionText, "The question should have text");
                assertFalse(questionText.isEmpty(), "The question text should not be empty");

                // Validate that options has at least one option if the question type is not TEXTUAL
                if (!"TEXTUAL".equals(type)) {
                    JsonNode options = question.get("options");
                    assertTrue(options.isArray() && options.size() > 0, "The options should have at least one option for non-TEXTUAL questions");
                }
            }
        }
    }

    private boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private JsonNode readJson(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(new File("src/test/resources/samples/" + fileName));
        } catch (IOException e) {
            fail("Failed to read JSON file: " + fileName);
            return null;
        }
    }
}
