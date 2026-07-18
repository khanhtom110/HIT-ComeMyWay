package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.AppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.dto.response.AppointmentDisplayResponse;
import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.entity.BookingStatus;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.AppointmentRepository;
import com.hit.comemyway.repository.ClinicRepository;
import com.hit.comemyway.repository.ServiceRepository;
import com.hit.comemyway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;
  private final ClinicRepository clinicRepository;
  private final ServiceRepository serviceRepository;

  @Transactional
  public AppointmentResponse createAppointment(AppointmentRequest request) {
    validateAppointmentTime(request.appointmentDate(), request.appointmentTime());

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    Clinic clinic = clinicRepository.findById(request.clinicId())
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    // Loai bo ID trung lap
    Set<Long> uniqueServiceIds = new HashSet<>(request.serviceIds());

    List<com.hit.comemyway.entity.Service> services =
        serviceRepository.findAllById(uniqueServiceIds);

    if (services.size() != uniqueServiceIds.size()) {
      throw new AppException(404, ErrorMessage.Appointment.APPOINTMENT_SERVICE_NOT_EXISTED);
    }

    Set<Long> clinicAvailableServiceIds = clinic.getServices().stream()
        .map(com.hit.comemyway.entity.Service::getId).collect(Collectors.toSet());

    if (!clinicAvailableServiceIds.containsAll(uniqueServiceIds)) {
      throw new AppException(404, ErrorMessage.Appointment.CLINIC_SERVICE_MISMATCH);
    }

    LocalTime requestedTime = request.appointmentTime();
    LocalTime clinicOpenTime = clinic.getOpenTime();
    LocalTime clinicCloseTime = clinic.getCloseTime();

    if (requestedTime.isBefore(clinicOpenTime) || requestedTime.isAfter(clinicCloseTime)) {
      throw new AppException(400, ErrorMessage.Appointment.OUT_OF_OPERATING_HOURS);
    }

    Appointment appointment = Appointment.builder().user(user).clinic(clinic)
        .fullName(request.fullName()).phone(request.phone()).bookingType(request.bookingType())
        .homeAddress(request.homeAddress()).petType(request.petType())
        .petCondition(request.petCondition()).petQuantity(request.petQuantity())
        .appointmentDate(request.appointmentDate()).appointmentTime(request.appointmentTime())
        .services(services).build();

    Appointment savedAppointment = appointmentRepository.save(appointment);
    return AppointmentResponse.from(savedAppointment);
  }

  @Transactional(readOnly = true)
  public List<AppointmentDisplayResponse> getUserAppointment() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    return appointmentRepository.findByUserId(user.getId()).stream()
        .map(AppointmentDisplayResponse::from).toList();
  }

  @Transactional
  public AppointmentResponse updateAppointment(AppointmentRequest request, Long appointmentId) {
    validateAppointmentTime(request.appointmentDate(), request.appointmentTime());

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Appointment.APPOINTMENT_NOT_EXISTED));

    if (!BookingStatus.PENDING.equals(appointment.getStatus())) {
      throw new AppException(400, ErrorMessage.Appointment.CANNOT_EDIT_APPOINTMENT);
    }

    if (!appointment.getUser().getId().equals(user.getId())) {
      throw new AppException(403, ErrorMessage.FORBIDDEN_UPDATE_DELETE);
    }

    Clinic clinic = clinicRepository.findById(request.clinicId())
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    // Loai bo ID trung lap
    Set<Long> uniqueServiceIds = new HashSet<>(request.serviceIds());

    List<com.hit.comemyway.entity.Service> services =
        serviceRepository.findAllById(uniqueServiceIds);

    if (services.size() != uniqueServiceIds.size()) {
      throw new AppException(404, ErrorMessage.Appointment.APPOINTMENT_SERVICE_NOT_EXISTED);
    }

    Set<Long> clinicAvailableServiceIds = clinic.getServices().stream()
        .map(com.hit.comemyway.entity.Service::getId).collect(Collectors.toSet());

    if (!clinicAvailableServiceIds.containsAll(uniqueServiceIds)) {
      throw new AppException(404, ErrorMessage.Appointment.CLINIC_SERVICE_MISMATCH);
    }

    LocalTime requestedTime = request.appointmentTime();
    LocalTime clinicOpenTime = clinic.getOpenTime();
    LocalTime clinicCloseTime = clinic.getCloseTime();

    if (requestedTime.isBefore(clinicOpenTime) || requestedTime.isAfter(clinicCloseTime)) {
      throw new AppException(400, ErrorMessage.Appointment.OUT_OF_OPERATING_HOURS);
    }

    appointment.setClinic(clinic);
    appointment.setFullName(request.fullName());
    appointment.setPhone(request.phone());
    appointment.setBookingType(request.bookingType());
    appointment.setHomeAddress(request.homeAddress());
    appointment.setPetType(request.petType());
    appointment.setPetCondition(request.petCondition());
    appointment.setPetQuantity(request.petQuantity());
    appointment.setAppointmentDate(request.appointmentDate());
    appointment.setAppointmentTime(request.appointmentTime());
    appointment.setServices(services);

    Appointment savedAppointment = appointmentRepository.save(appointment);
    return AppointmentResponse.from(savedAppointment);
  }

  private void validateAppointmentTime(LocalDate appointmentDate, LocalTime appointmentTime) {
    ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    LocalDate today = LocalDate.now(zoneId);
    LocalTime now = LocalTime.now(zoneId);

    if (appointmentDate.isBefore(today)) {
      throw new AppException(400, ErrorMessage.Appointment.INVALID_APPOINTMENT_DATE);
    }

    if (appointmentDate.isEqual(today) && appointmentTime.isBefore(now)) {
      throw new AppException(400, ErrorMessage.Appointment.INVALID_APPOINTMENT_TIME);
    }
  }

  @Transactional(readOnly = true)
  public AppointmentResponse getAppointmentDetail(Long id) {
    Appointment appointment = appointmentRepository.findByIdWithUserAndClinic(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Appointment.APPOINTMENT_NOT_EXISTED));

    return AppointmentResponse.from(appointment);
  }

  @Transactional
  public AppointmentResponse cancelAppoinment(Long id) {
    Appointment appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Appointment.APPOINTMENT_NOT_EXISTED));

    if (appointment.getStatus() != BookingStatus.PENDING) {
      throw new AppException(400, ErrorMessage.Appointment.ONLY_PENDING_CAN_BE_CANCELLED);
    }

    appointment.setStatus(BookingStatus.CANCELLED);

    Appointment updatedAppointment = appointmentRepository.save(appointment);

    return AppointmentResponse.from(updatedAppointment);
  }

  @Transactional(readOnly = true)
  public List<AppointmentResponse> getPendingStatusAppointment() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Clinic clinic = clinicRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    List<Appointment> appointments = appointmentRepository
        .findPendingAppointmentsByClinicId(clinic.getId(), BookingStatus.PENDING);

    return appointments.stream().map(AppointmentResponse::from).toList();
  }

  @Transactional
  public AppointmentResponse confirmAppointmentStatus(Long appointmentId) {
    Appointment appointment = getAppointmentByOwnerClinic(appointmentId);

    if (appointment.getStatus() != BookingStatus.PENDING) {
      throw new AppException(400, ErrorMessage.Appointment.ACTION_ONLY_FOR_PENDING);
    }

    appointment.setStatus(BookingStatus.CONFIRMED);
    return AppointmentResponse.from(appointment);
  }

  @Transactional
  public AppointmentResponse rejectAppointmentStatus(Long appointmentId, String rejectReason) {
    Appointment appointment = getAppointmentByOwnerClinic(appointmentId);

    if (appointment.getStatus() != BookingStatus.PENDING) {
      throw new AppException(400, ErrorMessage.Appointment.ACTION_ONLY_FOR_PENDING);
    }

    appointment.setStatus(BookingStatus.REJECTED);
    appointment.setRejectReason(rejectReason);
    return AppointmentResponse.from(appointment);
  }

  private Appointment getAppointmentByOwnerClinic(Long appointmentId) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Clinic clinic = clinicRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Appointment.APPOINTMENT_NOT_EXISTED));

    if (!appointment.getClinic().getId().equals(clinic.getId())) {
      throw new AppException(403, ErrorMessage.FORBIDDEN);
    }

    return appointment;
  }
}
