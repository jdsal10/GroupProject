package com.firstapp.group10app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class chatGPT extends AsyncTask<String, Void, String> {
    private static final String apiKey = "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";
    private static final String model = "gpt-3.5-turbo";
    private static final String url = "https://api.openai.com/v1/chat/completions";

    @Override
    protected String doInBackground(String... strings) {
        try {
            return chatGPT(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

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

        System.out.println(body); // For debugging purposes (remove later)

        // Get the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine); // For debugging purposes (remove later)
            response.append(inputLine);
        }
        in.close();

        System.out.println(response); // For debugging purposes (remove later)
        return response.toString(); // Return the response
    }
}