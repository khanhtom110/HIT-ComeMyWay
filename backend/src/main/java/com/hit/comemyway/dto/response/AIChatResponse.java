package com.hit.comemyway.dto.response;

import java.util.List;

public record AIChatResponse(
// @formatter:off
        String aiResponse,
        String suggestedServiceName,
        List<ClinicSuggestionResponse> recommendedClinics
) {
}
