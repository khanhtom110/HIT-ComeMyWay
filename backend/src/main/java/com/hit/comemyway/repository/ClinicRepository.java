package com.hit.comemyway.repository;

import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.entity.Clinic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

  @Query("SELECT DISTINCT c FROM Clinic c LEFT JOIN FETCH c.services s " + "WHERE c.status = true "
      + "AND (:keyword IS NULL OR :keyword = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
      + "AND c.latitude BETWEEN :minLatitude AND :maxLatitude "
      + "AND c.longitude BETWEEN :minLongitude AND :maxLongitude")
  List<Clinic> findClinicsWithLocation(@Param("keyword") String keyword,
      @Param("minLatitude") Double minLatitude, @Param("maxLatitude") Double maxLatitude,
      @Param("minLongitude") Double minLongitude, @Param("maxLongitude") Double maxLongitude);

  @Query("SELECT c FROM Clinic c WHERE c.status = true AND c.id IN ("
      + "SELECT DISTINCT c2.id FROM Clinic c2 LEFT JOIN c2.services s2 "
      + "WHERE (:keyword IS NULL OR :keyword = '' "
      + "OR LOWER(c2.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
      + "OR LOWER(c2.address) LIKE LOWER(CONCAT('%', :keyword, '%')) "
      + "OR LOWER(s2.name) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
  List<Clinic> findClinicsGlobal(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT c FROM Clinic c " + "WHERE c.status = true "
      + "AND (:keyword IS NULL OR :keyword = '' "
      + "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
      + "OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
  List<ClinicSuggestionResponse> findSuggestions(@Param("keyword") String keyword,
      Pageable pageable);
}
