package com.hit.comemyway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hit.comemyway.dto.response.ClinicPostResponse;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.ClinicPostReadRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ClinicPostReadServiceTest {
  private final ClinicPostReadRepository repository = mock(ClinicPostReadRepository.class);
  private final ClinicPostReadService service = new ClinicPostReadService(repository);

  private ClinicPostResponse post(long id, String content) {
    return new ClinicPostResponse(id, 7L, "FunPet", null, "Tiêu đề", content, null, List.of());
  }

  @Test
  void pageUsesExtraRowAndReturnsLastVisibleId() {
    when(repository.findPosts(null, 3))
        .thenReturn(List.of(post(9, "Nội dung"), post(8, "Bài hai"), post(7, "Bài ba")));
    var page = service.list(null, 2);
    assertEquals(List.of(9L, 8L), page.getContent().stream().map(p -> p.id()).toList());
    assertTrue(page.isHasNext());
    assertEquals(8L, page.getLastPostId());
    when(repository.findPosts(8L, 3)).thenReturn(List.of(post(7, "Bài ba")));
    assertFalse(service.list(8L, 2).isHasNext());
    verify(repository).findPosts(8L, 3);
  }

  @Test
  void emptyPageHasNoCursor() {
    when(repository.findPosts(null, 11)).thenReturn(List.of());
    var page = service.list(null, 10);
    assertTrue(page.getContent().isEmpty());
    assertFalse(page.isHasNext());
    assertNull(page.getLastPostId());
  }

  @Test
  void excerptDoesNotSplitUnicodeAndDetailKeepsFullContent() {
    String content = "🐾".repeat(181);
    var post = post(9, content);
    when(repository.findPosts(null, 11)).thenReturn(List.of(post));
    when(repository.findById(9)).thenReturn(Optional.of(post));
    assertEquals("🐾".repeat(180) + "…", service.list(null, 10).getContent().get(0).excerpt());
    assertEquals(content, service.detail(9).content());
  }

  @Test
  void deletedOrMissingPostReturns404() {
    when(repository.findById(9)).thenReturn(Optional.empty());
    assertEquals(404, assertThrows(AppException.class, () -> service.detail(9)).getErrorCode());
  }

  @Test
  void listUsesFirstImageAndDetailKeepsAllImagesInOrder() {
    var images = List.of("https://example.com/first.jpg", "https://example.com/second.jpg");
    var post = new ClinicPostResponse(9L, 7L, "FunPet", null, "Tiêu đề", "Nội dung", images.get(0),
        images);
    when(repository.findPosts(null, 11)).thenReturn(List.of(post));
    when(repository.findById(9)).thenReturn(Optional.of(post));
    assertEquals(images.get(0), service.list(null, 10).getContent().get(0).imageUrl());
    assertEquals(images, service.detail(9).imageUrls());
  }

  @Test
  void invalidCursorLimitAndIdNeverReachDatabase() {
    for (int limit : new int[] {0, -1, 51}) {
      assertEquals(400,
          assertThrows(AppException.class, () -> service.list(null, limit)).getErrorCode());
    }
    assertThrows(AppException.class, () -> service.list(0L, 10));
    assertThrows(AppException.class, () -> service.detail(0));
    verifyNoInteractions(repository);
  }
}
