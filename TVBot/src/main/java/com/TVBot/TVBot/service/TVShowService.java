package com.TVBot.TVBot.service;

import com.TVBot.TVBot.model.TVShow;
import com.TVBot.TVBot.repository.TVShowRepository;
import com.pgvector.PGvector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class TVShowService {
    private final TVShowRepository repository;
    private final EmbeddingService embeddingService ;
    @Autowired
    private DataSource dataSource;

    public TVShowService(TVShowRepository repository, EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
        this.repository = repository;
    }



    public void addTVShow(String title, String genre, String description, float[] embedding) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            PGvector.registerTypes(conn);
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO tv_shows (title, genre, description, embedding) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, description);
            pstmt.setObject(4, new PGvector(embedding));
            pstmt.executeUpdate();
        }
    }

    public List<TVShow> getAllShows() {
        return repository.findAll();
    }

    public TVShow findMostSimilarTVShow(float[] promptEmbedding) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            PGvector.registerTypes(conn);
            String sql = """
            SELECT *
            FROM tv_shows
            ORDER BY embedding <-> ?     -- For cosine distance (alternatively use <#> for L2)
            LIMIT 1
        """;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setObject(1, new PGvector(promptEmbedding));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    TVShow show = new TVShow();
                    show.setId(rs.getLong("id"));
                    show.setTitle(rs.getString("title"));
                    show.setGenre(rs.getString("genre"));
                    show.setDescription(rs.getString("description"));
                    // fill in other fields as necessary
                    return show;
                }
            }
        }
        return null;
    }


    public List<TVShow> findTopSimilarTVShows(float[] promptEmbedding, int n) throws Exception {
        List<TVShow> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            PGvector.registerTypes(conn);
            String sql = """
                        SELECT *
                        FROM tv_shows
                        ORDER BY embedding <-> ?
                        LIMIT ?
                    """;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setObject(1, new PGvector(promptEmbedding));
                pstmt.setInt(2, n);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    TVShow show = new TVShow();
                    show.setId(rs.getLong("id"));
                    show.setTitle(rs.getString("title"));
                    show.setGenre(rs.getString("genre"));
                    show.setDescription(rs.getString("description"));
                    // Add other fields if needed
                    result.add(show);
                }
            }
        }
        return result;
    }
}
