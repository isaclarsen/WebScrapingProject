package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordNotifier{

    private static final String DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1347130371497529354/AMAHMN3GO39f3OhNNtLmSukKAy2w3sfKqRaTgJZIaIe1HswduD0qtV5PSSRlGS0FxRKe";

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
        String leagueLogo = "https://brand.riotgames.com/static/a91000434ed683358004b85c95d43ce0/8a20a/lol-logo.png";

        ObjectMapper objectMapper = new ObjectMapper();

        //skapar json
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("username", "League of Legends");

        //Skapa webhook embed
        ObjectNode embed = objectMapper.createObjectNode();
        embed.putObject("author").put("name", "League Patch Notes");
        embed.put("color", "14177041");


        //Skapa fields lista
        ArrayNode fields = objectMapper.createArrayNode();

        //Patch
        ObjectNode patchField = objectMapper.createObjectNode();
        patchField.put("name", "Nya League of Legends nyheter!");
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

        ObjectNode footerField = objectMapper.createObjectNode();
        footerField.put("text", "@eshaag");

        //sätt in fälten i webhook embed
        embed.set("fields", fields);
        embed.set("footer", footerField);
        embed.putObject("thumbnail").put("url", leagueLogo);

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
