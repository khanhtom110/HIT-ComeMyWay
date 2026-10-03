package com.hit.comemyway.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hit.comemyway.dto.response.ClinicPostResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClinicPostReadRepository {
  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  private static final String SELECT = """
      SELECT p.id, p.clinic_id, c.name AS clinic_name,
             c.thumbnail_url AS clinic_avatar_url, p.title, p.content, p.image_urls
      FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id
      """;

  private RowMapper<ClinicPostResponse> mapper() {
    return (rs, rowNum) -> {
      List<String> images = readImages(rs.getString("image_urls"));
      return new ClinicPostResponse(rs.getLong("id"), rs.getLong("clinic_id"),
          rs.getString("clinic_name"), rs.getString("clinic_avatar_url"), rs.getString("title"),
          rs.getString("content"), images.isEmpty() ? null : images.get(0), images);
    };
  }

  private List<String> readImages(String json) throws SQLException {
    if (json == null) {
      return List.of();
    }
    try {
      List<String> images = objectMapper.readValue(json, new TypeReference<List<String>>() {});
      return images == null ? List.of() : List.copyOf(images);
    } catch (JsonProcessingException | NullPointerException exception) {
      throw new SQLException("Invalid clinic post image_urls", exception);
    }
  }

  public List<ClinicPostResponse> findPosts(Long lastPostId, int fetchSize) {
    if (lastPostId == null) {
      return jdbcTemplate.query(SELECT + " ORDER BY p.id DESC LIMIT ?", mapper(), fetchSize);
    }
    return jdbcTemplate.query(SELECT + " WHERE p.id < ? ORDER BY p.id DESC LIMIT ?", mapper(),
        lastPostId, fetchSize);
  }

  public Optional<ClinicPostResponse> findById(long id) {
    return jdbcTemplate.query(SELECT + " WHERE p.id = ?", mapper(), id).stream().findFirst();
  }
}
