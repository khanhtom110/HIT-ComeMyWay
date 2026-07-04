package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.AppointmentRequest;
import com.hit.comemyway.dto.response.AppointmentResponse;
import com.hit.comemyway.entity.Appointment;
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

@Service
@RequiredArgsConstructor
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;
  private final ClinicRepository clinicRepository;

  @Transactional
  public AppointmentResponse createAppointment(AppointmentRequest request) {
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
}
