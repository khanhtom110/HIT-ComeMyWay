package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.AppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.entity.BookingStatus;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.AppointmentRepository;
import com.hit.comemyway.repository.ClinicRepository;
import com.hit.comemyway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;
  private final ClinicRepository clinicRepository;

  @Transactional
  public AppointmentResponse createAppointment(AppointmentRequest request) {
    validateAppointmentTime(request.appointmentDate(), request.appointmentTime());

    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    Clinic clinic = clinicRepository.findById(request.clinicId())
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    Appointment appointment = Appointment.builder().user(user).clinic(clinic)
        .fullName(request.fullName()).phone(request.phone()).bookingType(request.bookingType())
        .homeAddress(request.homeAddress()).petType(request.petType())
        .petCondition(request.petCondition()).petQuantity(request.petQuantity())
        .appointmentDate(request.appointmentDate()).appointmentTime(request.appointmentTime())
        .build();

    Appointment savedAppointment = appointmentRepository.save(appointment);
    return AppointmentResponse.from(savedAppointment);
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

    Appointment savedAppointment = appointmentRepository.save(appointment);
    return AppointmentResponse.from(savedAppointment);
  }

  private void validateAppointmentTime(LocalDate appointmentDate, LocalTime appointmentTime) {
    // Ép múi giờ chuẩn của người dùng hệ thống (VD: Việt Nam)
    ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    LocalDate today = LocalDate.now(zoneId);
    LocalTime now = LocalTime.now(zoneId);

    // 1. Nếu ngày hẹn nằm trong quá khứ -> Lỗi
    if (appointmentDate.isBefore(today)) {
      throw new AppException(400, ErrorMessage.Appointment.INVALID_APPOINTMENT_DATE);
    }

    // 2. Nếu ngày hẹn là hôm nay, nhưng giờ hẹn đã qua -> Lỗi
    if (appointmentDate.isEqual(today) && appointmentTime.isBefore(now)) {
      throw new AppException(400, ErrorMessage.Appointment.INVALID_APPOINTMENT_TIME);
    }
  }
}
