package com.hit.comemyway.entity;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void homeAddressIsOptionalForNewAccounts() throws NoSuchFieldException {
    Column column = User.class.getDeclaredField("homeAddress").getAnnotation(Column.class);
    assertTrue(column.nullable());
    User user = User.builder().username("new-user").email("new-user@example.com").build();
    assertNull(user.getHomeAddress());
  }
}
