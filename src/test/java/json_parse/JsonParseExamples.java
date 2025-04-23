package json_parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JsonParseExamples {

    @Test
    void test1JsonFile() {
        JsonNode json = readJson("1.json");
        assertEquals(1, json.get("id").asInt());
        assertEquals("John Doe", json.get("name").asText());
        assertEquals(30, json.get("age").asInt());
        assertTrue(json.get("isMember").asBoolean());
    }

    @Test
    void test2JsonFile() {
        JsonNode json = readJson("2.json");

        assertEquals(2, json.get("id").asInt());
        assertEquals("Jane Doe", json.get("name").asText());
        assertFalse(json.get("isMember").asBoolean());

        // Extract roles array
        JsonNode rolesNode = json.get("roles");
        assertNotNull(rolesNode);
        assertTrue(rolesNode.isArray());

        List<String> roles = new ArrayList<>();
        rolesNode.forEach(role -> roles.add(role.asText()));

        assertEquals(List.of("user", "admin"), roles);

        // Contact object assertions
        JsonNode contact = json.get("contact");
        assertEquals("jane.doe@example.com", contact.get("email").asText());
        assertEquals("123-456-7890", contact.get("phone").asText());
    }

    @Test
    void test3JsonFile() {
        JsonNode json = readJson("3.json");

        assertEquals(3, json.get("id").asInt());
        assertEquals("Alice", json.get("name").asText());
        assertTrue(json.get("isMember").asBoolean());

        // Extract roles array
        JsonNode rolesNode = json.get("roles");
        assertNotNull(rolesNode);
        assertTrue(rolesNode.isArray());

        List<String> roles = new ArrayList<>();
        rolesNode.forEach(role -> roles.add(role.asText()));

        assertEquals(List.of("moderator"), roles);

        // Nested contact assertions
        JsonNode contact = json.get("contact");
        assertEquals("alice@example.com", contact.get("email").asText());
        assertEquals("987-654-3210", contact.get("phone").asText());
        assertEquals("123 Elm Street", contact.get("address").get("street").asText());
        assertEquals("Springfield", contact.get("address").get("city").asText());
        assertEquals("12345", contact.get("address").get("postalCode").asText());

        // Validate purchases array and total cost
        JsonNode purchases = json.get("purchases");
        assertEquals(2, purchases.size());

        double totalCost = 0.0;
        for (JsonNode purchase : purchases) {
            double price = purchase.get("price").asDouble();
            int quantity = purchase.get("quantity").asInt();
            totalCost += price * quantity;
        }

        assertEquals(2398.97, totalCost, 0.01);
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