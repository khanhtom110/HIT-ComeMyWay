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
    return new ClinicPostResponse(id, 7L, "FunPet", null, "Tiêu đề", content, null);
  }

  @Test
  void pageUsesExtraRowAndReturnsLastVisibleId() {
    when(repository.findPublished(null, 3))
        .thenReturn(List.of(post(9, "Nội dung"), post(8, "Bài hai"), post(7, "Bài ba")));
    var page = service.list(null, 2);
    assertEquals(List.of(9L, 8L), page.getContent().stream().map(p -> p.id()).toList());
    assertTrue(page.isHasNext());
    assertEquals(8L, page.getLastPostId());
    when(repository.findPublished(8L, 3)).thenReturn(List.of(post(7, "Bài ba")));
    assertFalse(service.list(8L, 2).isHasNext());
    verify(repository).findPublished(8L, 3);
  }

  @Test
  void emptyPageHasNoCursor() {
    when(repository.findPublished(null, 11)).thenReturn(List.of());
    var page = service.list(null, 10);
    assertTrue(page.getContent().isEmpty());
    assertFalse(page.isHasNext());
    assertNull(page.getLastPostId());
  }

  @Test
  void excerptDoesNotSplitUnicodeAndDetailKeepsFullContent() {
    String content = "🐾".repeat(181);
    var post = post(9, content);
    when(repository.findPublished(null, 11)).thenReturn(List.of(post));
    when(repository.findPublishedById(9)).thenReturn(Optional.of(post));
    assertEquals("🐾".repeat(180) + "…", service.list(null, 10).getContent().get(0).excerpt());
    assertEquals(content, service.detail(9).content());
  }

  @Test
  void hiddenDeletedOrMissingPostReturns404() {
    when(repository.findPublishedById(9)).thenReturn(Optional.empty());
    assertEquals(404, assertThrows(AppException.class, () -> service.detail(9)).getErrorCode());
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
