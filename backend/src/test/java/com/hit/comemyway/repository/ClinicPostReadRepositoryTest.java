package com.hit.comemyway.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hit.comemyway.dto.response.ClinicPostResponse;
import java.sql.ResultSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class ClinicPostReadRepositoryTest {
  private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
  private final ClinicPostReadRepository repository =
      new ClinicPostReadRepository(jdbc, new ObjectMapper());

  @Test
  @SuppressWarnings("unchecked")
  void cursorQueryHasNoStatusFilterAndBindsParameters() {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(20L), eq(11))).thenReturn(List.of());
    repository.findPosts(20L, 11);
    var sql = ArgumentCaptor.forClass(String.class);
    verify(jdbc).query(sql.capture(), any(RowMapper.class), eq(20L), eq(11));
    assertFalse(sql.getValue().contains("status"));
    assertTrue(sql.getValue().contains("p.image_urls"));
    assertTrue(sql.getValue().contains("p.id < ? ORDER BY p.id DESC LIMIT ?"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void firstPageAndDetailDoNotDependOnStatus() {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(11))).thenReturn(List.of());
    when(jdbc.query(anyString(), any(RowMapper.class), eq(42L))).thenReturn(List.of());
    assertTrue(repository.findPosts(null, 11).isEmpty());
    assertTrue(repository.findById(42).isEmpty());
    verify(jdbc).query(argThat(sql -> !sql.contains("status") && !sql.contains("WHERE")),
        any(RowMapper.class), eq(11));
    verify(jdbc).query(argThat(sql -> !sql.contains("status") && sql.contains("WHERE p.id = ?")),
        any(RowMapper.class), eq(42L));
  }

  @Test
  @SuppressWarnings("unchecked")
  void mapsContentAndAllowsMissingImage() throws Exception {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(11))).thenReturn(List.of());
    repository.findPosts(null, 11);
    ArgumentCaptor<RowMapper<ClinicPostResponse>> mapper = ArgumentCaptor.forClass(RowMapper.class);
    verify(jdbc).query(anyString(), mapper.capture(), eq(11));
    var rs = mock(ResultSet.class);
    when(rs.getString("content")).thenReturn("Nội dung đầy đủ");
    var post = mapper.getValue().mapRow(rs, 0);
    assertNotNull(post);
    assertEquals("Nội dung đầy đủ", post.content());
    assertNull(post.imageUrl());
    assertEquals(List.of(), post.imageUrls());
  }

  @Test
  @SuppressWarnings("unchecked")
  void mapsNodeImageJsonInOrderAndUsesFirstImageAsThumbnail() throws Exception {
    when(jdbc.query(anyString(), any(RowMapper.class), eq(11))).thenReturn(List.of());
    repository.findPosts(null, 11);
    ArgumentCaptor<RowMapper<ClinicPostResponse>> mapper = ArgumentCaptor.forClass(RowMapper.class);
    verify(jdbc).query(anyString(), mapper.capture(), eq(11));
    var rs = mock(ResultSet.class);
    when(rs.getString("image_urls"))
        .thenReturn("[\"https://example.com/first.jpg\",\"https://example.com/second.jpg\"]");
    var post = mapper.getValue().mapRow(rs, 0);
    assertEquals("https://example.com/first.jpg", post.imageUrl());
    assertEquals(List.of("https://example.com/first.jpg", "https://example.com/second.jpg"),
        post.imageUrls());
    when(rs.getString("image_urls")).thenReturn("[]");
    assertNull(mapper.getValue().mapRow(rs, 0).imageUrl());
    assertTrue(mapper.getValue().mapRow(rs, 0).imageUrls().isEmpty());
    when(rs.getString("image_urls")).thenReturn("null");
    assertTrue(mapper.getValue().mapRow(rs, 0).imageUrls().isEmpty());
    when(rs.getString("image_urls")).thenReturn("not-json");
    assertThrows(java.sql.SQLException.class, () -> mapper.getValue().mapRow(rs, 0));
  }
}
