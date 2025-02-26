package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordNotifier{

    private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1344319381324169246/LQrXMmjLkp77kKyElX1poWkKucVTtQsmbZZCJFr9RC47yiL5bi4_X9sGZpSB_nW4f5Yu";

    public static void sendToDiscord(String productName, String price, String url) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Create JSON object
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("username", "Price Scraper");
            payload.put("content", "Pris på StreetLab!");

            // Create embed structure
            ObjectNode embed = objectMapper.createObjectNode();
            embed.putObject("author").put("name", productName);

            // Create fields array
            ArrayNode fields = objectMapper.createArrayNode();

            ObjectNode priceField = objectMapper.createObjectNode();
            priceField.put("name", "Price");
            priceField.put("value", price);
            priceField.put("inline", true);
            fields.add(priceField);

            ObjectNode urlField = objectMapper.createObjectNode();
            urlField.put("name", "URL");
            urlField.put("value", url);
            fields.add(urlField);

            embed.set("fields", fields);

            // Add embeds to payload
            ArrayNode embeds = objectMapper.createArrayNode();
            embeds.add(embed);
            payload.set("embeds", embeds);

            // Convert JSON to string
            String jsonString = objectMapper.writeValueAsString(payload);

            // Send request to Discord
            sendHttpPost(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendHttpPost(String jsonPayload) throws Exception {
        URL url = new URL(DISCORD_WEBHOOK_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 204) {
            System.out.println("Message sent successfully!");
        } else {
            System.out.println("Failed to send message. Response code: " + responseCode);
        }
    }
}
