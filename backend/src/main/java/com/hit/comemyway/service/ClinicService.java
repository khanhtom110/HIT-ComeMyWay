package com.hit.comemyway.service;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.CompleteClinicProfileRequest;
import com.hit.comemyway.dto.response.*;
import com.hit.comemyway.entity.AccountStatus;
import com.hit.comemyway.entity.Clinic;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.ClinicRepository;
import com.hit.comemyway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClinicService {
  private final UserRepository userRepository;
  private final ClinicRepository clinicRepository;
  private final SearchClinicsService searchClinicsService;
  private final MapService mapService;
  private final ImageUploadService imageUploadService;
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
  public ClinicDetailResponse getClinicById(Long id, Double userLatitude, Double userLongitude) {
    Clinic clinic = clinicRepository.findById(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));
    LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    boolean isOperating = isOperating(clinic, now);

    // Xu ly khoang cach
    Double distance = null;
    if (userLatitude != null && userLongitude != null) {
      distance = searchClinicsService.calculateDistance(userLatitude, userLongitude, clinic);
    }

    return ClinicDetailResponse.from(clinic, isOperating, distance);
  }

  @Transactional(readOnly = true)
  public List<DefaultSuggestClinicResponse> getClinicSuggestions(Boolean status,
      Double userLatitude, Double userLongitude, Double radius, Integer limit) {
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
      Double userLongitude, Double radius, Integer limit) {
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

  private List<DefaultSuggestClinicResponse> mapToSuggestionResponses(List<Clinic> clinics,
      Double userLatitude, Double userLongitude) {
    List<DefaultSuggestClinicResponse> responses = new ArrayList<>();

    if (userLatitude != null && userLongitude != null && !clinics.isEmpty()) {
      List<Double> distances =
          searchClinicsService.calculateDistance(userLatitude, userLongitude, clinics);

      for (int i = 0; i < clinics.size(); i++) {
        Clinic clinic = clinics.get(i);

        Double distance = (i < distances.size()) ? distances.get(i) : null;

        responses.add(new DefaultSuggestClinicResponse(clinic.getId(), clinic.getName(),
            clinic.getAddress(), clinic.getThumbnailUrl(), distance, null));
      }

      if (userLatitude != null && userLongitude != null) {
        responses.sort(Comparator.comparing(DefaultSuggestClinicResponse::distance,
            Comparator.nullsLast(Comparator.naturalOrder())));
      }
    } else {
      for (Clinic clinic : clinics) {
        responses.add(new DefaultSuggestClinicResponse(clinic.getId(), clinic.getName(),
            clinic.getAddress(), clinic.getThumbnailUrl(), null, clinic.getRating()));
      }
    }
    return responses;
  }

  @Transactional(readOnly = true)
  public ClinicBookingResponse getClinicBookingById(Long id) {
    Clinic clinic = clinicRepository.findById(id)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));
    LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    boolean isOperating = isOperating(clinic, now);
    return ClinicBookingResponse.from(clinic, isOperating);
  }

  @Transactional(rollbackFor = Exception.class)
  public CompleteClinicProfileResponse completeClinicProfile(CompleteClinicProfileRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    if (clinicRepository.existsByUser(user)) {
      throw new AppException(400, ErrorMessage.Clinic.CLINIC_PROFILE_ALREADY_DONE);
    }

    checkClinicOperatingTime(request.openTime(), request.closeTime());

    Map<String, Double> coordinates = mapService.extractCoordinates(request.mapLink());

    user.setStatus(AccountStatus.ACTIVE);

    Clinic clinic = Clinic.builder().user(user).name(request.name()).address(request.address())
        .phone(request.phone()).mapLink(request.mapLink()).description(request.description())
        .latitude(coordinates.get("latitude")).longitude(coordinates.get("longitude"))
        .closeTime(request.closeTime()).openTime(request.openTime())
        .thumbnailUrl(request.thumbnailUrl()).build();

    List<com.hit.comemyway.entity.Service> services =
        request.services().stream().map(serviceName -> com.hit.comemyway.entity.Service.builder()
            .name(serviceName).clinic(clinic).build()).toList();

    clinic.getServices().addAll(services);

    clinicRepository.save(clinic);

    return CompleteClinicProfileResponse.from(clinic);
  }

  @Transactional
  public CompleteClinicProfileResponse updateClinicProfile(CompleteClinicProfileRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Clinic clinic = clinicRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Clinic.CLINIC_NOT_EXISTED));

    checkClinicOperatingTime(request.openTime(), request.closeTime());

    Map<String, Double> coordinates = mapService.extractCoordinates(request.mapLink());

    clinic.setName(request.name());
    clinic.setAddress(request.address());
    clinic.setPhone(request.phone());
    clinic.setMapLink(request.mapLink());
    clinic.setDescription(request.description());
    clinic.setLatitude(coordinates.get("latitude"));
    clinic.setLongitude(coordinates.get("longitude"));
    clinic.setCloseTime(request.closeTime());
    clinic.setOpenTime(request.openTime());
    clinic.setThumbnailUrl(request.thumbnailUrl());

    clinic.getServices().clear();

    List<com.hit.comemyway.entity.Service> services =
        request.services().stream().map(serviceName -> com.hit.comemyway.entity.Service.builder()
            .name(serviceName).clinic(clinic).build()).toList();

    clinic.getServices().addAll(services);

    return CompleteClinicProfileResponse.from(clinic);
  }

  private void checkClinicOperatingTime(LocalTime open, LocalTime close) {
    // 1. Kiểm tra bằng nhau
    if (open.equals(close)) {
      throw new AppException(400, ErrorMessage.Clinic.INVALID_WORKING_HOURS);
    }

    // 2. Nếu phòng khám muốn mở cửa qua đêm
    if (open.isAfter(close)) {
      throw new AppException(400, ErrorMessage.Clinic.INVALID_WORKING_HOURS);
    }
  }
}


