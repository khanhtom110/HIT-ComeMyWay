package com.hit.comemyway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hit.comemyway.entity.Clinic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchClinicsService {
  private final RestTemplate restTemplate;

  public List<Double> calculateDistance(Double userLatitude, Double userLongitude,
      List<Clinic> clinics) {
    if (userLongitude == null || userLatitude == null || clinics.isEmpty()) {
      return new ArrayList<>();
    }

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(userLongitude);
    stringBuilder.append(",");
    stringBuilder.append(userLatitude);

    for (Clinic clinic : clinics) {
      stringBuilder.append(";");
      stringBuilder.append(clinic.getLongitude());
      stringBuilder.append(",");
      stringBuilder.append(clinic.getLatitude());
    }

    String link = "http://router.project-osrm.org/table/v1/driving/" + stringBuilder
        + "?sources=0&annotations=distance";

    List<Double> result = new ArrayList<>();

    try {
      ResponseEntity<JsonNode> response = restTemplate.getForEntity(link, JsonNode.class);
      JsonNode body = response.getBody();

      if (body != null && body.has("distances")) {
        JsonNode distances = body.get("distances").get(0);

        for (int i = 1; i < distances.size(); i++) {
          double meters = distances.get(i).asDouble();
          double kilometers = meters / 1000.0;
          kilometers = Math.round(kilometers * 10.0) / 10.0;
          result.add(kilometers);
        }
      }
    } catch (Exception e) {
      // Co loi xay ra -> Tra ve fallback (* 1.3)
      result.clear();
      for (Clinic clinic : clinics) {
        double fallback = calculateFallback(userLatitude, userLongitude, clinic.getLatitude(),
            clinic.getLongitude());
        fallback = Math.round(fallback * 10.0) / 10.0;
        result.add(fallback);
      }
    }

    return result;
  }

  public double calculateFallback(Double userLatitude, Double userLongitude, Double clinicLatitude,
      Double clinicLongitude) {
    double distance =
        calculateAirDistance(userLatitude, userLongitude, clinicLatitude, clinicLongitude);

    // He so duong vong o Ha Noi la 1.3
    return distance * 1.3;
  }

  public double calculateAirDistance(Double userLatitude, Double userLongitude,
      Double clinicLatitude, Double clinicLongitude) {
    // công thức lượng giác Haversine
    // Ban kinh Trai Dat
    final int R = 6371;

    // Doi sang radian
    double deltaLatitude = Math.toRadians(clinicLatitude - userLatitude);
    double deltaLongitude = Math.toRadians(clinicLongitude - userLongitude);

    double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
        + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(clinicLatitude))
            * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }
}
