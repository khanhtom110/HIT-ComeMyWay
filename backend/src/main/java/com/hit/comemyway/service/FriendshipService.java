package com.hit.comemyway.service;

import com.hit.comemyway.constant.CommonConstant;
import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.dto.request.FindFriendRequest;
import com.hit.comemyway.dto.request.FriendRequest;
import com.hit.comemyway.dto.response.FindFriendResponse;
import com.hit.comemyway.dto.response.FriendResponse;
import com.hit.comemyway.entity.Friendship;
import com.hit.comemyway.entity.FriendshipStatus;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.repository.FriendshipRepository;
import com.hit.comemyway.repository.UserRepository;
import com.hit.comemyway.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
  public static final String LOCKET_LINK_REGEX =
      "^https://petlocket\\.com/add\\?code=[a-zA-Z0-9_.-]+$";

  private final FriendshipRepository friendshipRepository;
  private final UserRepository userRepository;
  private final SecurityUtils securityUtils;

  @Transactional
  public FriendResponse addFriendRequest(FriendRequest request) {
    User user = securityUtils.getCurrentUser();

    if (user.getId().equals(request.friendId())) {
      throw new AppException(400, ErrorMessage.Locket.CANNOT_ADD_FRIEND_TO_YOURSELF);
    }

    User friend = userRepository.findById(request.friendId())
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    Friendship friendship;

    Optional<Friendship> existedFriendship =
        friendshipRepository.findRelationship(user.getId(), request.friendId());
    if (existedFriendship.isPresent()) {
      friendship = existedFriendship.get();

      if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
        throw new AppException(400, ErrorMessage.Locket.ALREADY_FRIENDS);
      }

      if (friendship.getStatus() == FriendshipStatus.PENDING) {
        throw new AppException(400, ErrorMessage.Locket.FRIEND_REQUEST_ALREADY_EXISTS);
      }

      if (friendship.getStatus() == FriendshipStatus.REJECTED) {
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.PENDING);
      }
    } else {
      friendship =
          Friendship.builder().user(user).friend(friend).status(FriendshipStatus.PENDING).build();
    }

    Friendship savedFriendship = friendshipRepository.save(friendship);

    return FriendResponse.from(savedFriendship, user.getId());
  }

  @Transactional(readOnly = true)
  public List<FriendResponse> findPendingFriendship() {
    User currentUser = securityUtils.getCurrentUser();

    List<Friendship> pendingRequests =
        friendshipRepository.findByFriendIdAndStatus(currentUser.getId(), FriendshipStatus.PENDING);

    return pendingRequests.stream().map(f -> FriendResponse.from(f, currentUser.getId())).toList();
  }

  @Transactional
  public void acceptFriendRequest(Long requestId) {
    User currentUser = securityUtils.getCurrentUser();

    Friendship friendship = friendshipRepository.findById(requestId)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Locket.FRIEND_REQUEST_NOT_FOUND));

    // CurrentId moi duoc accept request
    if (!friendship.getFriend().getId().equals(currentUser.getId())) {
      throw new AppException(403, ErrorMessage.Locket.NOT_AUTHORIZED_TO_ACCEPT);
    }

    // Kiem tra PENDING
    if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
      throw new AppException(400, ErrorMessage.Locket.INVALID_FRIEND_REQUEST_STATUS);
    }

    // Gioi hanh 20 nguoi
    long countCurrentUserFriends =
        friendshipRepository.countAcceptedFriends(currentUser.getId(), FriendshipStatus.ACCEPTED);
    if (countCurrentUserFriends >= CommonConstant.Locket.LIMIT_FRIEND) {
      throw new AppException(400, ErrorMessage.Locket.CURRENT_USER_LIMIT_REACHED);
    }

    long countSenderFriends = friendshipRepository
        .countAcceptedFriends(friendship.getUser().getId(), FriendshipStatus.ACCEPTED);
    if (countSenderFriends >= CommonConstant.Locket.LIMIT_FRIEND) {
      throw new AppException(400, ErrorMessage.Locket.FRIEND_LIMIT_EXCEEDED);
    }

    friendship.setStatus(FriendshipStatus.ACCEPTED);
    friendshipRepository.save(friendship);
  }

  @Transactional
  public void rejectFriendRequest(Long requestId) {
    User currentUser = securityUtils.getCurrentUser();

    Friendship friendship = friendshipRepository.findById(requestId)
        .orElseThrow(() -> new AppException(404, ErrorMessage.Locket.FRIEND_REQUEST_NOT_FOUND));

    // CurrentId moi duoc reject request
    if (!friendship.getFriend().getId().equals(currentUser.getId())) {
      throw new AppException(403, ErrorMessage.Locket.NOT_AUTHORIZED_TO_REJECT);
    }

    // Kiem tra PENDING
    if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
      throw new AppException(400, ErrorMessage.Locket.INVALID_FRIEND_REQUEST_STATUS);
    }

    friendship.setStatus(FriendshipStatus.REJECTED);
    friendshipRepository.save(friendship);
  }

  @Transactional(readOnly = true)
  public List<FriendResponse> getAcceptedFriends() {
    User currentUser = securityUtils.getCurrentUser();

    List<Friendship> friendships =
        friendshipRepository.findAllAcceptedFriends(currentUser.getId(), FriendshipStatus.ACCEPTED);

    return friendships.stream().map(f -> FriendResponse.from(f, currentUser.getId())).toList();
  }

  @Transactional(readOnly = true)
  public FindFriendResponse findFriend(FindFriendRequest request) {
    if (!request.locketLink().matches(LOCKET_LINK_REGEX)) {
      throw new AppException(400, ErrorMessage.INVALID_LOCKET_LINK);
    }

    String locketCode = UriComponentsBuilder.fromUriString(request.locketLink()).build()
        .getQueryParams().getFirst("code");

    User user = userRepository.findBylocketCode(locketCode)
        .orElseThrow(() -> new AppException(404, ErrorMessage.User.USER_NOT_EXISTED));

    return FindFriendResponse.from(user);
  }

  @Transactional
  public void unfriend(Long friendId) {
    Long currentUserId = securityUtils.getCurrentUser().getId();

    if (currentUserId.equals(friendId)) {
      throw new AppException(400, ErrorMessage.Locket.CANNOT_UNFRIEND_TO_YOURSELF);
    }

    boolean isFriend = friendshipRepository.existsFriendship(currentUserId, friendId);
    if (!isFriend) {
      throw new AppException(404, ErrorMessage.Locket.ARE_NOT_FRIENDS);
    }

    friendshipRepository.deleteFriendship(currentUserId, friendId);
  }
}
