package com.hit.comemyway.service;

import com.hit.comemyway.entity.Appointment;
import com.hit.comemyway.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentReminderService {
    private final AppointmentRepository appointmentRepository;
    private final FirebaseMessagingService fcmService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendAppointmentReminder() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        LocalDateTime targetDateTime = LocalDateTime.now(zoneId)
                .plusMinutes(30)
                .withSecond(0)
                .withNano(0);

        LocalDate targetDate = targetDateTime.toLocalDate();
        LocalTime targetTime = targetDateTime.toLocalTime();

        List<Appointment> upcomingAppointment = appointmentRepository.findAppointmentByDateAndTimeAndIsNotified(targetDate, targetTime);

        if (upcomingAppointment.isEmpty()) {
            return;
        }

        for (Appointment appointment : upcomingAppointment) {
            try {
                String deviceToken = appointment.getUser().getDeviceToken();

                if (deviceToken == null || deviceToken.isEmpty()) {
                    System.err.println("User " + appointment.getUser().getUsername() + " does not have device token");
                    continue;
                }

                String title = "Lịch hẹn sắp diễn ra";

                String body = String.format("Bạn có lịch hẹn vào lúc %s. Hãy chuẩn bị nhé!",
                        appointment.getAppointmentTime());

                fcmService.sendNotification(deviceToken, title, body);

                appointment.setIsNotified(true);
                appointmentRepository.save(appointment);

            } catch (Exception e) {
                System.err.println("Failure when sending notification for appointment: " + appointment.getId() + " - Exception: " + e.getMessage());
            }
        }
    }
}
