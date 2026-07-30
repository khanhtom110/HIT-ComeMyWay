package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.PetLocketCreateRequest;
import com.hit.comemyway.dto.response.PetLocketResponse;
import com.hit.comemyway.service.PetLocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
      description = "Sử dụng Slice phân trang tối ưu hiệu năng")
  @GetMapping(UrlConstant.Locket.NEWS_FEED)
  public ResponseEntity<ApiResponse<Slice<PetLocketResponse>>> getNewsFeed(
      @PageableDefault(size = 10) Pageable pageable) {
    Slice<PetLocketResponse> feed = petLocketService.getNewsFeed(pageable);
    return ResponseEntity.ok(ApiResponse.ok(feed));
  }

  @Operation(summary = "Lấy danh sách bài viết của chính mình")
  @GetMapping(UrlConstant.Locket.MY_POSTS)
  public ResponseEntity<ApiResponse<Slice<PetLocketResponse>>> getMyPosts(
      @PageableDefault(size = 10) Pageable pageable) {
    Slice<PetLocketResponse> posts = petLocketService.getMyPosts(pageable);
    return ResponseEntity.ok(ApiResponse.ok(posts));
  }
}
