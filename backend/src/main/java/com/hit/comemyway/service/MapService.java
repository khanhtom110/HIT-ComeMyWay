package com.hit.comemyway.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MapService {
  private static final Pattern DATA_PATTERN =
      Pattern.compile("!3d(-?\\d+\\.\\d+)!4d(-?\\d+\\.\\d+)");

  public Map<String, Double> extractCoordinates(String url) {
    Map<String, Double> coordinates = new HashMap<>();

    Matcher matcherData = DATA_PATTERN.matcher(url);
    if (matcherData.find()) {
      coordinates.put("latitude", Double.parseDouble(matcherData.group(1)));
      coordinates.put("longitude", Double.parseDouble(matcherData.group(2)));
      return coordinates;
    }

    throw new IllegalArgumentException("No valid coordinates found in this URL.");
  }
}
