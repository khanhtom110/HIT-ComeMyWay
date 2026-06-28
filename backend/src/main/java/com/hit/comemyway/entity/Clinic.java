package com.hit.comemyway.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "clinics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clinic extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
  private User user;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name = "thumbnail_url", length = 500)
  private String thumbnailUrl;

  @Column(length = 15)
  private String phone;

  @Column(nullable = false, length = 250)
  private String address;

  @Builder.Default
  private Double rating = 0.0;

  private Double latitude;

  private Double longitude;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "open_time")
  private LocalTime openTime;

  @Column(name = "close_time")
  private LocalTime closeTime;

  @Builder.Default
  private Boolean status = true;

  @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)
  @BatchSize(size = 20)
  @Builder.Default
  private List<Service> services = new ArrayList<>();

  @Column(name = "map_link", columnDefinition = "TEXT", nullable = false)
  private String mapLink;
}
