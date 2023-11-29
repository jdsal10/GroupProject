package com.firstapp.group10app.ChatGPT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPT_Client {
    private static final String apiKey = "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";
    private static final String model = "gpt-3.5-turbo";
    private static final String url = "https://api.openai.com/v1/chat/completions";

    public static String chatGPT(String input) throws Exception {
        // Connect to the API
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);

        // Set the request body
        String prompt = "[{\"role\": \"user\", \"content\": \"" + input + "\"}]";
        int maxTokens = 150;
        String body = "{\"model\": \"" + model + "\", \"messages\": " + prompt + ", \"max_tokens\": " + maxTokens + "}";

        // Send the request
        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();

        // Get the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder responseRaw = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseRaw.append(inputLine);
        }
        in.close();

        // extract the text from the response
        int start = responseRaw.indexOf("content\": \"") + 11;
        int end = responseRaw.indexOf("\"finish_reason\":") - 15;

        return responseRaw.substring(start, end); // Return the response
    }
}