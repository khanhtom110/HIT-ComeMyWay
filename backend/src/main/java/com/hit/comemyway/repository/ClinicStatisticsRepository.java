package com.hit.comemyway.repository;

import com.hit.comemyway.dto.response.ClinicStatisticsResponse;
import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.entity.Role;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClinicStatisticsRepository extends JpaRepository<Clinic, Long> {
  @Query("""
      SELECT new com.hit.comemyway.dto.response.ClinicStatisticsResponse(u.id, c.id, u.email)
      FROM User u
      LEFT JOIN Clinic c ON c.user = u
      WHERE u.role = :role AND u.status = :activeStatus
        AND (:keyword IS NULL OR LOWER(u.email) LIKE :keyword)
        AND (:cursor IS NULL OR u.id < :cursor)
      ORDER BY u.id DESC
      """)
  List<ClinicStatisticsResponse> activeClinicList(@Param("role") Role role,
      @Param("activeStatus") AccountStatus activeStatus, @Param("keyword") String keyword,
      @Param("cursor") Long cursor, Pageable pageable);

  @Query("""
      SELECT new com.hit.comemyway.dto.response.ClinicStatisticsResponse(u.id, c.id, u.email)
      FROM User u
      LEFT JOIN Clinic c ON c.user = u
      WHERE u.role = :role AND (u.status IS NULL OR u.status <> :activeStatus)
        AND (:keyword IS NULL OR LOWER(u.email) LIKE :keyword)
        AND (:cursor IS NULL OR u.id < :cursor)
      ORDER BY u.id DESC
      """)
  List<ClinicStatisticsResponse> inactiveClinicList(@Param("role") Role role,
      @Param("activeStatus") AccountStatus activeStatus, @Param("keyword") String keyword,
      @Param("cursor") Long cursor, Pageable pageable);
}
