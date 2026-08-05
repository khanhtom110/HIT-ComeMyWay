package com.hit.comemyway.dto.response;

import com.hit.comemyway.entity.Friendship;
import com.hit.comemyway.entity.User;

public record FriendResponse(
// @formatter:off
        Long friendshipId,
        Long friendId,
        String username,
        String avatar
) {
    public static FriendResponse from(Friendship friendship, Long currentUserId) {
        //Lay ra nguoi ban chu khong phai la ban than minh (o trong ban ghi)
        User friend = friendship.getUser().getId().equals(currentUserId)
                ? friendship.getFriend()
                : friendship.getUser();

        return new FriendResponse(
                friendship.getId(),
                friend.getId(),
                friend.getUsername(),
                friend.getAvatar()
        );
    }
}
