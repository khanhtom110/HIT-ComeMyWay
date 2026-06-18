package com.hit.comemyway.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidatedToken {
    @Id
    private String id;

    private Date expiryTime;
}