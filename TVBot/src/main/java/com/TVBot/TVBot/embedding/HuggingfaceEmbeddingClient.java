package com.TVBot.TVBot.embedding;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HuggingfaceEmbeddingClient {
    private static final String API_URL =
            "https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2";
    private static final String API_KEY = ""; // put your token here

    public static float[] getEmbedding(String text) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = "{\"inputs\": \"" + text + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String resp = response.body();

        // Parse response (it's a JSON array of array)
        // You can use Jackson or any other JSON parser
        ObjectMapper mapper = new ObjectMapper();
        float[][] arr = mapper.readValue(resp, float[][].class);
        return arr[0];
    }

    public static void main(String[] args) throws Exception {
        float[] embedding = getEmbedding("Hello! Recommend me a sci-fi show.");
        System.out.println(java.util.Arrays.toString(embedding));
        // Now you can store this embedding or use it for vector search
    }
}
