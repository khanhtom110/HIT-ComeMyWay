package com.hit.comemyway.entity;

import com.hit.comemyway.constant.CommonConstant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "clinic_id", nullable = false)
  private Clinic clinic;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "appointment_services", joinColumns = @JoinColumn(name = "appointment_id"),
      inverseJoinColumns = @JoinColumn(name = "service_id"))
  private List<Service> services;

  @Column(name = "full_name", nullable = false, length = CommonConstant.FULLNAME_LENGTH)
  private String fullName;

  @Column(name = "phone", nullable = false, length = CommonConstant.PHONE_LENGTH)
  private String phone;

  @Column(name = "booking_type", nullable = false)
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private BookingType bookingType = BookingType.AT_CLINIC;

  @Column(name = "home_address", length = CommonConstant.ADDRESS_LENGTH)
  private String homeAddress;

  @Column(name = "pet_type", nullable = false)
  private String petType;

  @Column(name = "pet_condition", length = CommonConstant.CONDITION_LENGTH)
  private String petCondition;

  @Column(name = "pet_quantity", nullable = false)
  private Integer petQuantity;

  @Column(name = "appointment_date", nullable = false)
  private LocalDate appointmentDate;

  @Column(name = "appointment_time", nullable = false)
  private LocalTime appointmentTime;

  @Column(name = "status", nullable = false)
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private BookingStatus status = BookingStatus.PENDING;

  @Column(name = "is_notified", nullable = false)
  @Builder.Default
  private Boolean isNotified = false;

  @Column(name = "reject_reason")
  private String rejectReason;
}
