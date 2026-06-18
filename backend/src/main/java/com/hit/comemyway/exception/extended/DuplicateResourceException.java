package com.hit.comemyway.exception.extended;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends AppException {
  private final String fieldName;

  public DuplicateResourceException(String resource, String field, Object value) {
    super(409, String.format("%s đã tồn tại với %s: '%s'", resource, field, value));
    this.fieldName = field;
  }
}
