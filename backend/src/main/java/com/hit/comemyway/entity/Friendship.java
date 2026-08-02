package com.hit.comemyway.entity;

import com.hit.comemyway.constant.CommonConstant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friendships",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_friend", columnNames = {"user_id", "friend_id"})},
    indexes = {@Index(name = "index_user_status", columnList = "user_id, status"),
        @Index(name = "index_friend_status", columnList = "friend_id, status")})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Friendship extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "friend_id", nullable = false)
  private User friend;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = CommonConstant.User.FRIENDSHIP_STATUS_LENGTH)
  private FriendshipStatus status;
}
