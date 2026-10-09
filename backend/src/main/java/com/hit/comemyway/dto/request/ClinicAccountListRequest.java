package com.hit.comemyway.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClinicAccountListRequest {
  @NotBlank(message = "activation is required.")
  @Pattern(regexp = "active|inactive", message = "activation must be active or inactive.")
  @Schema(
      description = "Only ACTIVE accounts belong to active; all other statuses belong to inactive.",
      allowableValues = {"active", "inactive"}, example = "active")
  private String activation;

  @Size(max = 255, message = "keyword must not exceed 255 characters.")
  @Schema(description = "Search by account email.", example = "funpet")
  private String keyword;

  @NotNull(message = "limit is required.")
  @Min(value = 1, message = "limit must be at least 1.")
  @Max(value = 50, message = "limit must not exceed 50.")
  @Schema(description = "Maximum number of accounts per page.", defaultValue = "20")
  private Integer limit = 20;

  @Positive(message = "cursor must be greater than 0.")
  @Schema(description = "nextCursor from the previous response. Omit for the first page.")
  private Long cursor;
}
