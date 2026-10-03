package com.hit.comemyway.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.hit.comemyway.dto.response.ClinicPostResponse;
import java.sql.ResultSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class ClinicPostReadRepositoryTest {
  private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
  private final ClinicPostReadRepository repository = new ClinicPostReadRepository(jdbc);

  @Test
  @SuppressWarnings("unchecked")
  void cursorQueryFiltersPublishedRowsAndBindsParameters() {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(20L), eq(11))).thenReturn(List.of());
    repository.findPublished(20L, 11);
    var sql = ArgumentCaptor.forClass(String.class);
    verify(jdbc).query(sql.capture(), any(RowMapper.class), eq(20L), eq(11));
    assertTrue(sql.getValue().contains("p.status = 'PUBLISHED'"));
    assertTrue(sql.getValue().contains("p.id < ? ORDER BY p.id DESC LIMIT ?"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void firstPageAndDetailKeepPublishedFilter() {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(11))).thenReturn(List.of());
    when(jdbc.query(anyString(), any(RowMapper.class), eq(42L))).thenReturn(List.of());
    assertTrue(repository.findPublished(null, 11).isEmpty());
    assertTrue(repository.findPublishedById(42).isEmpty());
    verify(jdbc).query(contains("p.status = 'PUBLISHED'"), any(RowMapper.class), eq(11));
    verify(jdbc).query(contains("AND p.id = ?"), any(RowMapper.class), eq(42L));
  }

  @Test
  @SuppressWarnings("unchecked")
  void mapsContentAndAllowsMissingImage() throws Exception {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(11))).thenReturn(List.of());
    repository.findPublished(null, 11);
    ArgumentCaptor<RowMapper<ClinicPostResponse>> mapper = ArgumentCaptor.forClass(RowMapper.class);
    verify(jdbc).query(anyString(), mapper.capture(), eq(11));
    var rs = mock(ResultSet.class);
    when(rs.getString("content")).thenReturn("Nội dung đầy đủ");
    var post = mapper.getValue().mapRow(rs, 0);
    assertNotNull(post);
    assertEquals("Nội dung đầy đủ", post.content());
    assertNull(post.imageUrl());
  }
}
