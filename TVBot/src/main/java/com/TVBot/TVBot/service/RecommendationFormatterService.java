package com.TVBot.TVBot.service;

import com.TVBot.TVBot.model.TVShow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationFormatterService {

    private final ChatClient chatClient;

    public RecommendationFormatterService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String formatRecommendations(List<TVShow> shows, String userPrompt) {
        StringBuilder sb = new StringBuilder();
        sb.append("The user is interested in: ").append(userPrompt).append("\n");
        sb.append("Recommend these TV shows with brief descriptions and a friendly tone:\n");
        int idx = 1;
        for (TVShow show : shows) {
            sb.append(idx++).append(". ")
                    .append(show.getTitle()).append(" (").append(show.getGenre()).append("): ")
                    .append(show.getDescription()).append("\n");
        }
        String prompt = sb.toString();
        UserMessage message = new UserMessage(prompt);
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
