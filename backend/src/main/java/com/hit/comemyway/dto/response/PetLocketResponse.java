package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.PetLocket;

import java.time.Instant;

public record PetLocketResponse(
// @formatter:off
        Long id,
        Long userId,
        String username,
        String userAvatar,
        String imageUrl,
        String caption,
        Instant createdAt
) {
    public static PetLocketResponse from(PetLocket petLocket) {
        return new PetLocketResponse(
                petLocket.getId(),
                petLocket.getUser().getId(),
                petLocket.getUser().getUsername(),
                petLocket.getUser().getAvatar(),
                petLocket.getImageUrl(),
                petLocket.getCaption(),
                petLocket.getCreatedAt()
        );
    }
}