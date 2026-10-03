package com.hit.comemyway.service;

import com.hit.comemyway.dto.response.ClinicPostListItemResponse;
import com.hit.comemyway.dto.response.ClinicPostResponse;
import com.hit.comemyway.dto.response.CursorResponse;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.ClinicPostReadRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClinicPostReadService {
  private final ClinicPostReadRepository repository;

  public CursorResponse<ClinicPostListItemResponse> list(Long lastPostId, int limit) {
    if (limit < 1 || limit > 50 || (lastPostId != null && lastPostId <= 0)) {
      throw new AppException(400, "limit phải từ 1 đến 50 và lastPostId phải lớn hơn 0");
    }
    List<ClinicPostResponse> rows = repository.findPublished(lastPostId, limit + 1);
    List<ClinicPostListItemResponse> items = rows.stream().limit(limit)
        .map(post -> new ClinicPostListItemResponse(post.id(), post.clinicId(), post.clinicName(),
            post.clinicAvatarUrl(), post.title(), excerpt(post.content()), post.imageUrl()))
        .toList();
    return CursorResponse.<ClinicPostListItemResponse>builder().content(items)
        .hasNext(rows.size() > limit)
        .lastPostId(items.isEmpty() ? null : items.get(items.size() - 1).id()).build();
  }

  public ClinicPostResponse detail(long id) {
    if (id <= 0) {
      throw new AppException(400, "ID bài viết phải lớn hơn 0");
    }
    return repository.findPublishedById(id)
        .orElseThrow(() -> new AppException(404, "Không tìm thấy bài viết"));
  }

  private static String excerpt(String content) {
    String text = content.replaceAll("\\s+", " ").trim();
    int length = text.codePointCount(0, text.length());
    return length <= 180 ? text : text.substring(0, text.offsetByCodePoints(0, 180)) + "…";
  }
}
