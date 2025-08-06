package com.TVBot.TVBot.dto;

import com.TVBot.TVBot.model.TVShow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {
    private String summary;
    private List<TVShow> shows;
}
