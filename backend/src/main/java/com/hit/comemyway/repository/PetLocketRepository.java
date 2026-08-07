package com.hit.comemyway.repository;

import com.hit.comemyway.entity.PetLocket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetLocketRepository extends JpaRepository<PetLocket, Long> {
  // Lay danh sach cua tat ca
  @Query("""
          SELECT p
          FROM PetLocket p
          JOIN FETCH p.user
          WHERE p.user.id IN :targetUserIds
            AND (:lastPostId IS NULL OR p.id < :lastPostId)
          ORDER BY p.id DESC
      """)
  Slice<PetLocket> findFeedByTargetUserIds(@Param("targetUserIds") List<Long> targetUserIds,
      @Param("lastPostId") Long lastPostId, Pageable pageable);

  @Query("""
      SELECT p
      FROM PetLocket p
      JOIN FETCH p.user
      WHERE p.user.id = :userId
        AND (:lastPostId IS NULL OR p.id < :lastPostId)
      ORDER BY p.id DESC
      """)
  Slice<PetLocket> findMyPostsByCursor(@Param("userId") Long userId,
      @Param("lastPostId") Long lastPostId, Pageable pageable);

}
