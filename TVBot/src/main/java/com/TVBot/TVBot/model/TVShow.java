package com.TVBot.TVBot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tv_shows")
@Getter
@Setter
public class TVShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String description;

    // We'll make the embedding a String for now.
    // (pgvector is not directly supported by JPA yet in most stacks)
    // We'll store the vector as text, e.g., "[0.12, -0.57, ...]"
    private String embedding;

}
