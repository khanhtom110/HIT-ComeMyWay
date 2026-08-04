package com.hit.comemyway.repository;

import com.hit.comemyway.entity.Friendship;
import com.hit.comemyway.entity.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
  // Kiem tra quan he cua 2 user
  @Query("""
              SELECT f FROM Friendship f
              WHERE (f.user.id = :userId1 AND f.friend.id = :userId2)
                  OR(f.user.id = :userId2 AND f.friend.id = :userId1)
      """)
  Optional<Friendship> findRelationship(@Param("userId1") Long userId1,
      @Param("userId2") Long userId2);

  // Dem so nguoi ban
  @Query("""
              SELECT COUNT(f) FROM Friendship f
              WHERE f.status = :status
                  AND (f.user.id = :userId
                      OR f.friend.id = :userId)
      """)
  long countAcceptedFriends(@Param("userId") Long userId, @Param("status") FriendshipStatus status);

  // Danh sach cho ket ban
  List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status);

  @Query("""
          SELECT f FROM Friendship f
          WHERE f.status = :status
          AND (f.user.id = :userId OR f.friend.id = :userId)
      """)
  List<Friendship> findAllAcceptedFriends(@Param("userId") Long userId,
      @Param("status") FriendshipStatus status);

  @Modifying
  @Query("""
     DELETE FROM Friendship f
     WHERE ((f.user.id = :userId AND f.friend.id = :friendId)
        OR (f.user.id = :friendId AND f.friend.id = :userId))
        AND f.status = 'ACCEPTED'
     """)
  void deleteFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);

  @Query("""
     SELECT COUNT(f) > 0
     FROM Friendship f
     WHERE ((f.user.id = :userId AND f.friend.id = :friendId)
       OR (f.user.id = :friendId AND f.friend.id = :userId))
       AND f.status = 'ACCEPTED'
     """)
  boolean existsFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
