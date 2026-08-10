package com.hit.comemyway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hit.comemyway.dto.response.AIChatResponse;
import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {
  private final GeminiService geminiService;
  private final ClinicRepository clinicRepository;
  private final ObjectMapper objectMapper;

  public AIChatResponse processAIChat(String userMessage) {
    // Thiet lap prompt
    String systemPrompt =
        """
            Bạn là trợ lý y tế thông minh cho ứng dụng đặt lịch phòng khám cho thú cưng.
            Nhiệm vụ của bạn:
            1. Trả lời/tư vấn sơ bộ và ân cần về triệu chứng mà người dùng cung cấp.
            2. Trích xuất ra đúng MỘT tên dịch vụ y tế phù hợp nhất có trong danh sách sau để chữa trị triệu chứng đó:
               [
                     "Khám",
                     "Chữa bệnh",
                     "Tiêm phòng vaccine",
                     "Xét nghiệm",
                     "Siêu âm",
                     "Phẫu thuật",
                     "Triệt sản",
                     "Spa",
                     "Grooming",
                     "Cắt tỉa",
                     "Lấy cao răng",
                     "Khách sạn thú cưng",
                     "Tư vấn chăm sóc",
                     "Điều trị bệnh nội ngoại trú",
                     "Đỡ đẻ",
                     "Mổ đẻ",
                     "Chụp X-quang",
                     "Vận chuyển thú cưng",
                     "Tắm",
                     "Tẩy giun",
                     "Phòng trị ve rận"
                   ]

            Bạn PHẢI trả về kết quả dưới dạng chuỗi JSON thuần túy (không bọc trong markdown code block như ```json ... ```), theo đúng cấu trúc sau:
            {
              "advice": "Câu trả lời tư vấn sơ bộ cho người dùng...",
              "serviceName": "Tên dịch vụ khớp với danh sách trên"
            }
            """;

    String fullPrompt = systemPrompt + "\n\nTriệu chứng của người dùng: " + userMessage;

    // Lay du lieu tho tu AI
    String rawAIResponse = geminiService.callGemini(fullPrompt);

    String aiAdvice = "Đang cập nhật tư vấn từ AI...";
    String detectedService = "";

    try {
      // Làm sạch chuỗi phòng hờ AI trả về kèm ký tự markdown
      String cleanedJson = rawAIResponse.replace("```json", "").replace("```", "").trim();
      JsonNode jsonNode = objectMapper.readTree(cleanedJson);

      aiAdvice = jsonNode.path("advice").asText(aiAdvice);
      detectedService = jsonNode.path("serviceName").asText("");
    } catch (Exception e) {
      aiAdvice = rawAIResponse; // Nếu AI trả về text thuần không phải JSON
    }

    // Dung ten dich vu AI tra ve de query
    List<Clinic> matchedClinics = List.of();
    if (!detectedService.isEmpty()) {
      matchedClinics = clinicRepository.findByServices_NameContainingIgnoreCase(detectedService);
    }

    List<ClinicSuggestionResponse> clinicResponses =
        matchedClinics.stream().map(clinic -> new ClinicSuggestionResponse(clinic.getId(),
            clinic.getName(), clinic.getAddress(), clinic.getThumbnailUrl())).limit(3).toList();

    return new AIChatResponse(aiAdvice, detectedService, clinicResponses);
  }
}
