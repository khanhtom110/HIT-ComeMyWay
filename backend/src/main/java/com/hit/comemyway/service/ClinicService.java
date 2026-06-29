package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.response.ClinicDetailResponse;
import com.hit.comemyway.dto.response.ClinicSearchResponse;
import com.hit.comemyway.dto.response.ClinicSuggestionResponse;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
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
  private static final double ONE_LATITUDE = 111.045;
  private static final int NUMBER_OSRM = 15;

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
    LocalTime open = clinic.getOpenTime();
    LocalTime close = clinic.getCloseTime();

    if (open == null || close == null) {
      return false;
    }

    // Ca trong ngay
    if (!close.isBefore(open)) {
      // True neu 'now' >= 'open' va 'now' <= 'close'
      return !now.isBefore(open) && !now.isAfter(close);
    }
    // Ca dem
    else {
      // true neu 'now' >= 'open' hoac 'now' <= 'close'
      return !now.isBefore(open) || !now.isAfter(close);
    }
  }

  @Transactional(readOnly = true)
  public List<ClinicSuggestionResponse> getSuggestions(String keyword, int limit) {
    String validKeyword = null;
    if (keyword != null && !keyword.trim().isEmpty()) {
      validKeyword = keyword.trim();
    }

    return clinicRepository.findSuggestions(validKeyword, PageRequest.of(0, limit));
  }

  @Transactional(readOnly = true)
  public ClinicDetailResponse getClinicById(Long id) {
    Clinic clinic = clinicRepository.findById(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));
    LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    boolean isOperating = isOperating(clinic, now);
    return ClinicDetailResponse.from(clinic, isOperating);
  }

  @Transactional(readOnly = true)
  public List<ClinicSuggestionResponse> getClinicSuggestions(Boolean status, Double userLatitude,
      Double userLongitude, Double radius, int limit) {
    List<Clinic> clinics;

    if (userLatitude != null && userLongitude != null) {
      clinics =
          fetchClinicsForSuggestionWithLocation(status, userLatitude, userLongitude, radius, limit);
    } else {
      clinics = clinicRepository.findByStatusOrderByRatingDesc(status, PageRequest.of(0, limit));
    }

    return mapToSuggestionResponses(clinics, userLatitude, userLongitude);
  }

  private List<Clinic> fetchClinicsForSuggestionWithLocation(Boolean status, Double userLatitude,
      Double userLongitude, Double radius, int limit) {
    double deltaLatitude = radius / ONE_LATITUDE;
    double deltaLongitude = radius / (ONE_LATITUDE * Math.cos(Math.toRadians(userLatitude)));

    double minLatitude = userLatitude - deltaLatitude;
    double maxLatitude = userLatitude + deltaLatitude;
    double minLongitude = userLongitude - deltaLongitude;
    double maxLongitude = userLongitude + deltaLongitude;

    List<Clinic> clinics = clinicRepository.findClinicsByStatusWithLocation(status, minLatitude,
        maxLatitude, minLongitude, maxLongitude);

    if (clinics.size() > limit) {
      clinics.sort((c1, c2) -> {
        double dist1 = searchClinicsService.calculateAirDistance(userLatitude, userLongitude,
            c1.getLatitude(), c1.getLongitude());
        double dist2 = searchClinicsService.calculateAirDistance(userLatitude, userLongitude,
            c2.getLatitude(), c2.getLongitude());
        return Double.compare(dist1, dist2);
      });

      clinics = clinics.subList(0, limit);
    }
    return clinics;
  }

  private List<ClinicSuggestionResponse> mapToSuggestionResponses(List<Clinic> clinics,
      Double userLatitude, Double userLongitude) {
    List<ClinicSuggestionResponse> responses = new ArrayList<>();

    if (userLatitude != null && userLongitude != null && !clinics.isEmpty()) {
      List<Double> distances =
          searchClinicsService.calculateDistance(userLatitude, userLongitude, clinics);

      List<Pair<Clinic, Double>> tempPairs = new ArrayList<>();
      for (int i = 0; i < clinics.size(); i++) {
        Double distance = (i < distances.size()) ? distances.get(i) : null;
        tempPairs.add(Pair.of(clinics.get(i), distance));
      }

      tempPairs.sort(
          Comparator.comparing(Pair::getSecond, Comparator.nullsLast(Comparator.naturalOrder())));

      for (Pair<Clinic, Double> pair : tempPairs) {
        Clinic c = pair.getFirst();
        responses.add(new ClinicSuggestionResponse(c.getId(), c.getName(), c.getAddress(),
            c.getThumbnailUrl()));
      }
    } else {
      for (Clinic clinic : clinics) {
        responses.add(new ClinicSuggestionResponse(clinic.getId(), clinic.getName(),
            clinic.getAddress(), clinic.getThumbnailUrl()));
      }
    }
    return responses;
  }
}
