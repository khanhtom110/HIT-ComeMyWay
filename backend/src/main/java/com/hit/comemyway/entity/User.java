package com.hit.comemyway.entity;

import com.hit.comemyway.constant.CommonConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false, length = CommonConstant.USERNAME_LENGTH,
      unique = true)
  private String username;

  @Column(nullable = false, length = CommonConstant.PASSWORD_LENGTH)
  private String password;

  @Column(length = CommonConstant.FULLNAME_LENGTH)
  private String fullName;

  @Column(length = CommonConstant.PHONE_LENGTH)
  private String phone;

  @Column(nullable = false, unique = true, length = CommonConstant.EMAIL_LENGTH)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;
}
