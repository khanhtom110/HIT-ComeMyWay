package com.hit.comemyway.controller;

import com.hit.comemyway.base.ApiResponse;
import com.hit.comemyway.constant.ApiPath;
import com.hit.comemyway.constant.SuccessMessage;
import com.hit.comemyway.constant.UrlConstant;
import com.hit.comemyway.dto.request.FindFriendRequest;
import com.hit.comemyway.dto.request.FriendRequest;
import com.hit.comemyway.dto.response.FindFriendResponse;
import com.hit.comemyway.dto.response.FriendResponse;
import com.hit.comemyway.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.API_V1)
@RequiredArgsConstructor
@Tag(name = "Friendship", description = "API cho module friendship")
public class FriendshipController {
  private final FriendshipService friendshipService;

  @Operation(summary = "Gửi yêu cầu kết bạn", description = "User gửi kết bạn cho người khác")
  @PostMapping(UrlConstant.Locket.ADD_FRIEND)
  public ResponseEntity<ApiResponse<FriendResponse>> addFriendRequest(
      @Valid @RequestBody FriendRequest request) {
    FriendResponse response = friendshipService.addFriendRequest(request);
    return ResponseEntity.ok(ApiResponse.ok(response));
  }

  @Operation(summary = "Lấy danh sách chờ kết bạn",
      description = "User có thể lấy danh sách chờ kết bạn")
  @GetMapping(UrlConstant.Locket.PENDING)
  public ResponseEntity<ApiResponse<List<FriendResponse>>> findPendingFriendship() {
    List<FriendResponse> responses = friendshipService.findPendingFriendship();
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Accept lời mời kết bạn", description = "User có thể đồng ý lời mời kết bạn")
  @PostMapping(UrlConstant.Locket.ACCEPT_REQUEST)
  public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(@PathVariable Long requestId) {
    friendshipService.acceptFriendRequest(requestId);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }

  @Operation(summary = "Reject lời mời kết bạn",
      description = "User có thể từ chối lời mời kết bạn")
  @PostMapping(UrlConstant.Locket.REJECT_REQUEST)
  public ResponseEntity<ApiResponse<Void>> rejectFriendRequest(@PathVariable Long requestId) {
    friendshipService.rejectFriendRequest(requestId);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }

  @Operation(summary = "Lấy danh sách bạn bè",
      description = "User có thể lấy danh sách bạn bè của mình")
  @GetMapping(UrlConstant.Locket.MY_FRIEND)
  public ResponseEntity<ApiResponse<List<FriendResponse>>> getMyFriend() {
    List<FriendResponse> responses = friendshipService.getAcceptedFriends();
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Tìm bạn qua link locket", description = "Tìm bạn qua link locket")
  @PostMapping(UrlConstant.Locket.FIND_FRIEND)
  public ResponseEntity<ApiResponse<FindFriendResponse>> findFriend(
      @Valid @RequestBody FindFriendRequest request) {
    FindFriendResponse responses = friendshipService.findFriend(request);
    return ResponseEntity.ok(ApiResponse.ok(responses));
  }

  @Operation(summary = "Hủy  kết bạn locket", description = "Hủy kết bạn locket")
  @PostMapping(UrlConstant.Locket.UNFRIEND)
  public ResponseEntity<ApiResponse<Void>> unfriend(@PathVariable Long friendId) {
    friendshipService.unfriend(friendId);
    return ResponseEntity.ok(ApiResponse.ok(SuccessMessage.Locket.UNFRIEND_SUCCESS, null));
  }
}
