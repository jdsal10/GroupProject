package com.firstapp.group10app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class chatGPT extends AsyncTask<String, Void, String> {
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
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);

        // The request body
//        JSONObject data = new JSONObject();
//        data.put("model", model);
//        data.put("messages", input);
//        data.put("max_tokens", 150);
////        data.put("temperature", 1.0);
        String prompt = "[{\"role\": \"user\", \"content\": \"" + input + "\"}]";
        int maxTokens = 150;
        String body = "{\"model\": \"" + model + "\", \"messages\": " + prompt + ", \"max_tokens\": " + maxTokens + "}";

        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();

        System.out.println(body);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response);
        return response.toString();
    }
}