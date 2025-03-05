package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static void leaguePatchNotifier(String patch, String date, String url) throws Exception {
        String eshaagUrl = "https://assets.vogue.com/photos/5891f06eb482c0ea0e4dbbdf/master/pass/slideshow-kurt-cobain-last-session-jesse-frohman-07.jpeg";

        ObjectMapper objectMapper = new ObjectMapper();

        //skapar json
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("username", "League Patch Notes");

        //Skapa webhook embed
        ObjectNode embed = objectMapper.createObjectNode();
        embed.putObject("author").put("name", "@eshaag");
        embed.put("color", "14177041");


        //Skapa fields lista
        ArrayNode fields = objectMapper.createArrayNode();

        //Patch
        ObjectNode patchField = objectMapper.createObjectNode();
        patchField.put("name", "Ny uppdatering till League har släppts");
        patchField.put("value", patch);
        patchField.put("inline", true);
        fields.add(patchField);

        //URL
        ObjectNode urlField = objectMapper.createObjectNode();
        urlField.put("name", "URL");
        urlField.put("value", url);
        fields.add(urlField);

        //Date
        ObjectNode dateField = objectMapper.createObjectNode();
        dateField.put("name", "Date");
        dateField.put("value", date);
        fields.add(dateField);

        //sätt in fälten i webhook embed
        embed.set("fields", fields);
        embed.putObject("thumbnail").put("url", eshaagUrl);

        ArrayNode embeds = objectMapper.createArrayNode();
        embeds.add(embed);
        payload.set("embeds", embeds);

        String jsonString = objectMapper.writeValueAsString(payload);

        sendHttpPost(jsonString);
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
