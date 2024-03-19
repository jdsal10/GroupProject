package com.firstapp.group10app.ChatGPT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is responsible for interacting with the OpenAI GPT-3 API.
 * It sends a chat message to the API and retrieves the response.
 */
public class ChatGptClient {
    private static final String apiKey = "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";
    private static final String model = "gpt-3.5-turbo";
    private static final String url = "https://api.openai.com/v1/chat/completions";

    /**
     * Sends a chat message to the OpenAI GPT-3 API and retrieves the response.
     * @param input The chat message to send to the API.
     * @return The response from the API.
     * @throws Exception If an error occurs while interacting with the API.
     */
    public static String chatGPT(String input) throws Exception {
        // Connect to the API
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);

        // Set the request body
        String prompt = "[{\"role\": \"user\", \"content\": \"" + input + "\"}]";
        int maxTokens = 600;
        String body = "{\"model\": \"" + model + "\", \"messages\": " + prompt + ", \"max_tokens\": " + maxTokens + "}";

        // Send the request
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        System.out.println("Message: " + body);

        writer.write(body);
        writer.flush();

        // Get the response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder responseRaw = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseRaw.append(inputLine);
        }
        in.close();

        // Extract the text from the response
        int start = responseRaw.indexOf("content\": \"") + 11;
        int end = responseRaw.indexOf("\"finish_reason\":") - 15;

        return responseRaw.substring(start, end); // Return the response
    }
}