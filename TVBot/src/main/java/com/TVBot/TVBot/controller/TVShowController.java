package com.TVBot.TVBot.controller;

import com.TVBot.TVBot.dto.TVShowDto;
import com.TVBot.TVBot.model.TVShow;
import com.TVBot.TVBot.service.EmbeddingService;
import com.TVBot.TVBot.service.TVShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController  // Use RestController for JSON API
@RequestMapping("/api/tvshows")
public class TVShowController {

    private final TVShowService service;
    private final EmbeddingService embeddingService;

    public TVShowController(TVShowService service, EmbeddingService embeddingService) {
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
    public ResponseEntity<List<TVShow>> recommend(@RequestBody Map<String, String> body, @RequestParam(defaultValue = "5") int count){
        try {
            String prompt = body.get("prompt");
            float[] promptEmbedding = embeddingService.generateEmbedding(prompt);
            List<TVShow> similarShow = service.findTopSimilarTVShows(promptEmbedding,count);
            return ResponseEntity.ok(similarShow);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
