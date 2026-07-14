package com.hit.comemyway.repository;

import com.hit.comemyway.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  @Query("""
      SELECT a
      FROM Appointment a
      JOIN FETCH a.clinic
      LEFT JOIN FETCH a.services
      WHERE a.user.id = :userId
      ORDER BY a.updatedAt DESC
      """)
  List<Appointment> findByUserId(@Param("userId") Long userId);

  Optional<Appointment> findById(Long id);

  @Query("""
      SELECT a
      FROM Appointment a
      WHERE a.appointmentDate = :date
      AND a.appointmentTime = :time
      AND a.isNotified = false
      """)
  List<Appointment> findAppointmentByDateAndTimeAndIsNotified(
  // @formatter:off
        @Param("date") LocalDate date,
        @Param("time") LocalTime time
    // @formatter:on
  );

  @Query("""
      SELECT a
      FROM Appointment a
      JOIN FETCH a.user
      JOIN FETCH a.clinic
      WHERE a.id = :id
      """)
  Optional<Appointment> findByIdWithUserAndClinic(@Param("id") Long id);
}
