package com.TVBot.TVBot.controller;

import com.TVBot.TVBot.dto.RecommendationResponse;
import com.TVBot.TVBot.dto.TVShowDto;
import com.TVBot.TVBot.model.TVShow;
import com.TVBot.TVBot.service.EmbeddingService;
import com.TVBot.TVBot.service.RecommendationFormatterService;
import com.TVBot.TVBot.service.TVShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController  // Use RestController for JSON API
@RequestMapping("/api/tvshows")
public class TVShowController {

    private final TVShowService service;
    private final EmbeddingService embeddingService;
    private final RecommendationFormatterService formatterService;

    public TVShowController(TVShowService service, EmbeddingService embeddingService, RecommendationFormatterService formatterService) {
        this.formatterService = formatterService;
        this.embeddingService = embeddingService;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addShow(@RequestBody TVShowDto showDto) {
        try {
            // 1. Combine relevant fields for embedding
            String textForEmbedding = showDto.getTitle() + " " + showDto.getDescription();

            // 2. Generate the embedding using the local Ollama model (via Spring AI)
            float[] embedding = embeddingService.generateEmbedding(textForEmbedding);

            // 3. Insert into DB (via service, which uses JDBC and PGvector)
            service.addTVShow(
                    showDto.getTitle(),
                    showDto.getGenre(),
                    showDto.getDescription(),
                    embedding
            );

            return ResponseEntity.ok("TV show added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add TV show: " + e.getMessage());
        }
    }

    @GetMapping
    public List<TVShow> listShows() {
        return service.getAllShows();
    }

    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponse> recommend(@RequestBody Map<String, String> body, @RequestParam(defaultValue = "1") int count){
        try {
            String prompt = body.get("prompt");
            float[] promptEmbedding = embeddingService.generateEmbedding(prompt);
            List<TVShow> similarShow = service.findTopSimilarTVShows(promptEmbedding,count);

            String summary = formatterService.formatRecommendations(similarShow, prompt);

            RecommendationResponse response = new RecommendationResponse();
            response.setSummary(summary);
            response.setShows(similarShow);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
