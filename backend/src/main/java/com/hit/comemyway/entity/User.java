package com.hit.comemyway.entity;

import com.hit.comemyway.constant.RoleConstant;
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

    @Column(nullable = false, updatable = false, length = 120, unique = true)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(length = 255)
    private String fullName;

    @Column(length = 10)
    private String phone;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column
    private String role;
}
