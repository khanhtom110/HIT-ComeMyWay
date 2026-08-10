package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.PetLocketCreateRequest;
import com.hit.comemyway.dto.response.CursorResponse;
import com.hit.comemyway.dto.response.PetLocketResponse;
import com.hit.comemyway.service.PetLocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.API_V1)
@RequiredArgsConstructor
@Tag(name = "Pet Locket - Feed", description = "API đăng bài và lấy tin tức Newsfeed")
public class PetLocketController {

  private final PetLocketService petLocketService;

  @Operation(summary = "Tạo bài đăng Locket mới")
  @PostMapping(UrlConstant.Locket.CREATE_POST)
  public ResponseEntity<ApiResponse<PetLocketResponse>> createPost(
      @Valid @RequestBody PetLocketCreateRequest request) {
    PetLocketResponse response = petLocketService.createPost(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy Newsfeed (Bài viết của mình + bạn bè)",
      description = "Sử dụng Cursor-based Pagination để cuộn vô tận")
  @GetMapping(UrlConstant.Locket.NEWS_FEED)
  public ResponseEntity<ApiResponse<CursorResponse<PetLocketResponse>>> getNewsFeed(@Parameter(
      description = "ID của bài viết cuối cùng trong danh sách hiện tại (để trống nếu lấy lần đầu)")
  @RequestParam(required = false) Long lastPostId, @RequestParam(defaultValue = "10") int size) {
    // Gioi han khi nhap qua lon -> lay ve 30
    int validatedSize = Math.clamp(size, 1, 30);
    Slice<PetLocketResponse> feed = petLocketService.getNewsFeed(lastPostId, validatedSize);

    Long nextCursor = feed.getContent().isEmpty() ? null
        : feed.getContent().get(feed.getContent().size() - 1).id();

    CursorResponse<PetLocketResponse> response = CursorResponse.of(feed, nextCursor);

    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy danh sách bài viết của chính mình",
      description = "Sử dụng Cursor-based Pagination để cuộn vô tận")
  @GetMapping(UrlConstant.Locket.MY_POSTS)
  public ResponseEntity<ApiResponse<CursorResponse<PetLocketResponse>>> getMyPosts(@Parameter(
      description = "ID của bài viết cuối cùng trong danh sách hiện tại (để trống nếu lấy lần đầu)")
  @RequestParam(required = false) Long lastPostId, @RequestParam(defaultValue = "10") int size) {
    int validatedSize = Math.clamp(size, 1, 30);
    Slice<PetLocketResponse> posts = petLocketService.getMyPosts(lastPostId, validatedSize);

    Long nextCursor = posts.getContent().isEmpty() ? null
        : posts.getContent().get(posts.getContent().size() - 1).id();

    CursorResponse<PetLocketResponse> response = CursorResponse.of(posts, nextCursor);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Xóa ảnh locket", description = "Xóa ảnh locket")
  @PostMapping(UrlConstant.Locket.DELETE_POST)
  public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
    petLocketService.deletePost(postId);
    return ResponseEntity.ok(ApiResponse.ok(SuccessMessage.Locket.DELETE_POST_SUCCESS, null));
  }
}
