package com.hit.comemyway.service;

import com.hit.comemyway.dto.request.PetLocketCreateRequest;
import com.hit.comemyway.dto.response.PetLocketResponse;
import com.hit.comemyway.entity.Friendship;
import com.hit.comemyway.entity.FriendshipStatus;
import com.hit.comemyway.entity.PetLocket;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.repository.FriendshipRepository;
import com.hit.comemyway.repository.PetLocketRepository;
import com.hit.comemyway.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetLocketService {
  private final PetLocketRepository petLocketRepository;
  private final FriendshipRepository friendshipRepository;
  private final SecurityUtils securityUtils;

  @Transactional
  public PetLocketResponse createPost(PetLocketCreateRequest request) {
    User currentUser = securityUtils.getCurrentUser();

    PetLocket petLocket = PetLocket.builder().user(currentUser).imageUrl(request.imageUrl())
        .caption(request.caption()).build();

    petLocketRepository.save(petLocket);
    return PetLocketResponse.from(petLocket);
  }

  @Transactional(readOnly = true)
  public Slice<PetLocketResponse> getNewsFeed(Long lastPostId, int size) {
    User currentUser = securityUtils.getCurrentUser();

    List<Friendship> allAcceptedFriends =
        friendshipRepository.findAllAcceptedFriends(currentUser.getId(), FriendshipStatus.ACCEPTED);

    List<Long> targetUserIds = new java.util.ArrayList<>(allAcceptedFriends.stream()
        .map(friendship -> extractFriendId(friendship, currentUser.getId())).toList());

    targetUserIds.add(currentUser.getId());

    Pageable pageable = PageRequest.of(0, size);

    Slice<PetLocket> petLockets =
        petLocketRepository.findFeedByTargetUserIds(targetUserIds, lastPostId, pageable);

    return petLockets.map(PetLocketResponse::from);
  }

  private Long extractFriendId(Friendship friendship, Long currentUserId) {
    return friendship.getUser().getId().equals(currentUserId) ? friendship.getFriend().getId()
        : friendship.getUser().getId();
  }

  @Transactional(readOnly = true)
  public Slice<PetLocketResponse> getMyPosts(Long lastPostId, int size) {
    User currentUser = securityUtils.getCurrentUser();
    Pageable pageable = PageRequest.of(0, size);
    Slice<PetLocket> petLockets =
        petLocketRepository.findMyPostsByCursor(currentUser.getId(), lastPostId, pageable);
    return petLockets.map(PetLocketResponse::from);
  }
}
