package com.firstapp.group10app;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class chatGPT_API {
    public static String chatGPT(String prompt) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);

        // The request body
        JSONObject data = new JSONObject();
        data.put("model", model);
        data.put("prompt", prompt);
        data.put("max_tokens", 150);
        data.put("temperature", 1.0);

        connection.setDoOutput(true);
        connection.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}