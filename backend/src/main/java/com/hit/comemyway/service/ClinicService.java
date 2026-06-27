package com.hit.comemyway.service;

import com.hit.comemyway.dto.response.ClinicSearchResponse;
import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
  private final ClinicRepository clinicRepository;
  private final SearchClinicsService searchClinicsService;
  private final double ONE_LATITUDE = 111.045;
  private final int NUMBER_OSRM = 15;

  @Transactional(readOnly = true)
  public List<ClinicSearchResponse> findClinics(String keyword, Double userLatitude,
      Double userLongitude, Double radius, int limit) {
    List<Clinic> clinics;

    if (userLatitude != null && userLongitude != null) {
      clinics = fetchClinicsWithLocation(keyword, userLatitude, userLongitude, radius);
    } else {
      clinics = clinicRepository.findClinicsGlobal(keyword, PageRequest.of(0, limit));
    }

    return mapToResponses(clinics, userLatitude, userLongitude);
  }

  private List<Clinic> fetchClinicsWithLocation(String keyword, Double userLatitude,
      Double userLongitude, Double radius) {
    double deltaLatitude = radius / ONE_LATITUDE;
    double deltaLongitude = radius / (ONE_LATITUDE * Math.cos(Math.toRadians(userLatitude)));

    double minLatitude = userLatitude - deltaLatitude;
    double maxLatitude = userLatitude + deltaLatitude;
    double minLongitude = userLongitude - deltaLongitude;
    double maxLongitude = userLongitude + deltaLongitude;

    List<Clinic> clinics = clinicRepository.findClinicsWithLocation(keyword, minLatitude,
        maxLatitude, minLongitude, maxLongitude);

    if (clinics.size() > NUMBER_OSRM) {
      clinics.sort((c1, c2) -> {
        double dist1 = searchClinicsService.calculateAirDistance(userLatitude, userLongitude,
            c1.getLatitude(), c1.getLongitude());
        double dist2 = searchClinicsService.calculateAirDistance(userLatitude, userLongitude,
            c2.getLatitude(), c2.getLongitude());
        return Double.compare(dist1, dist2);
      });

      // Lay NUMBER_OSRM phong kham -> tranh spam OSRM
      clinics = clinics.subList(0, NUMBER_OSRM);
    }
    return clinics;
  }

  private List<ClinicSearchResponse> mapToResponses(List<Clinic> clinics, Double userLatitude,
      Double userLongitude) {
    List<Double> distances = (userLatitude != null && userLongitude != null)
        ? searchClinicsService.calculateDistance(userLatitude, userLongitude, clinics)
        : new ArrayList<>();

    LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    List<ClinicSearchResponse> responses = new ArrayList<>();

    for (int i = 0; i < clinics.size(); i++) {
      Clinic clinic = clinics.get(i);

      Double distance = (i < distances.size()) ? distances.get(i) : null;

      responses.add(new ClinicSearchResponse(clinic.getId(), clinic.getThumbnailUrl(),
          clinic.getName(), isOperating(clinic, now), clinic.getRating(), distance,
          clinic.getAddress(), clinic.getOpenTime(), clinic.getCloseTime(),
          clinic.getServices().stream().map(service -> service.getName()).toList()));
    }

    // Sap xep khoang cach tang dan, neu distance = null thi tu dong day xuong last index
    if (userLatitude != null && userLongitude != null) {
      responses.sort(Comparator.comparing(ClinicSearchResponse::distance,
          Comparator.nullsLast(Comparator.naturalOrder())));
    }
    return responses;
  }

  private boolean isOperating(Clinic clinic, LocalTime now) {
    return clinic.getOpenTime() != null && clinic.getCloseTime() != null
        && now.isAfter(clinic.getOpenTime()) && now.isBefore(clinic.getCloseTime());
  }

  @Transactional(readOnly = true)
  public List<ClinicSuggestionResponse> getSuggestions(String keyword, int limit) {
    String validKeyword = null;
    if (keyword != null && !keyword.trim().isEmpty()) {
      validKeyword = keyword.trim();
    }

    return clinicRepository.findSuggestions(validKeyword, PageRequest.of(0, limit));
  }
}
