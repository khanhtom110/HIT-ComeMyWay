package com.hit.comemyway.controller;

import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V1)
@Tag(name = "Media", description = "Các API Quản lý tài nguyên, upload hình ảnh và file")
public class ImageController {
  private final ImageUploadService imageUploadService;

  @PostMapping(UrlConstant.Media.UPLOAD)
  @Operation(summary = "Upload hình ảnh lên Cloudinary",
      description = "Nhận vào một file ảnh và tải lên hệ thống lưu trữ Cloudinary. Trả về đường dẫn URL an toàn của ảnh để lưu vào cơ sở dữ liệu.")
  public ResponseEntity<String> upload(
      @Parameter(description = "File ảnh cần tải lên", required = true)
      @RequestParam("file") MultipartFile file) throws IOException {
    String url = imageUploadService.uploadImage(file);
    return ResponseEntity.ok(url);
  }
}

