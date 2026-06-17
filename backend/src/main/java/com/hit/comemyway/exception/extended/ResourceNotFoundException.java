package com.hit.comemyway.exception.extended;

public class ResourceNotFoundException extends AppException {

  public ResourceNotFoundException(String resource, String fieldName, Object value) {
    super(404, String.format("%s không tìm thấy với %s: '%s'", resource, fieldName, value));
  }
}
