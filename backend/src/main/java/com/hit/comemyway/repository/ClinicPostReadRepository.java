package com.hit.comemyway.repository;

import com.hit.comemyway.dto.response.ClinicPostResponse;
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

  private static final String SELECT = """
      SELECT p.id, p.clinic_id, c.name AS clinic_name,
             c.thumbnail_url AS clinic_avatar_url, p.title, p.content, p.image_url
      FROM clinic_posts p JOIN clinics c ON c.id = p.clinic_id
      WHERE p.status = 'PUBLISHED'
      """;

  private static final RowMapper<ClinicPostResponse> MAPPER =
      (rs, rowNum) -> new ClinicPostResponse(rs.getLong("id"), rs.getLong("clinic_id"),
          rs.getString("clinic_name"), rs.getString("clinic_avatar_url"), rs.getString("title"),
          rs.getString("content"), rs.getString("image_url"));

  public List<ClinicPostResponse> findPublished(Long lastPostId, int fetchSize) {
    if (lastPostId == null) {
      return jdbcTemplate.query(SELECT + " ORDER BY p.id DESC LIMIT ?", MAPPER, fetchSize);
    }
    return jdbcTemplate.query(SELECT + " AND p.id < ? ORDER BY p.id DESC LIMIT ?", MAPPER,
        lastPostId, fetchSize);
  }

  public Optional<ClinicPostResponse> findPublishedById(long id) {
    return jdbcTemplate.query(SELECT + " AND p.id = ?", MAPPER, id).stream().findFirst();
  }
}
