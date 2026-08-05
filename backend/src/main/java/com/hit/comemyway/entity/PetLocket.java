package com.hit.comemyway.entity;

import com.hit.comemyway.constant.CommonConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_lockets",
    indexes = {@Index(name = "index_user_create_at", columnList = "user_id, created_at DESC")}

)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PetLocket extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = CommonConstant.User.IMAGE_URL_LENGTH)
  private String imageUrl;

  @Column(length = CommonConstant.Locket.LIMIT_LENGTH_CAPTION)
  private String caption;
}
