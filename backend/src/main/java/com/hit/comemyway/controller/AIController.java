package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.dto.request.AIChatRequest;
import com.hit.comemyway.dto.response.AIChatResponse;
import com.hit.comemyway.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AI Medical Chatbot",
    description = "API tư vấn triệu chứng và gợi ý phòng khám bằng AI")
@RequestMapping(ApiPath.API_V1)
public class AIController {
  private final AIService aiService;

  @Operation(summary = "Chat với AI bác sĩ",
      description = "Gửi triệu chứng, nhận tư vấn và danh sách phòng khám gợi ý")
  @PostMapping("/chat")
  public ResponseEntity<ApiResponse<AIChatResponse>> chatWithAI(
      @Valid @RequestBody AIChatRequest request) {
    AIChatResponse response = aiService.processAIChat(request.message());
    return ResponseEntity.ok(ApiResponse.ok(response));
  }
}
