package com.be.sportizebe.domain.chat.controller;

import com.be.sportizebe.domain.chat.dto.response.ChatRoomResponse;
import com.be.sportizebe.domain.chat.entity.ChatRoom;
import com.be.sportizebe.domain.chat.service.ChatRoomService;
import com.be.sportizebe.domain.post.entity.Post;
import com.be.sportizebe.domain.post.repository.PostRepository;
import com.be.sportizebe.domain.user.entity.User;
import com.be.sportizebe.domain.user.repository.UserRepository;
import com.be.sportizebe.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/note")
@Tag(name = "note", description = "쪽지 관련 API")
public class NoteController {

    private final ChatRoomService chatRoomService;
    private final PostRepository postRepository;
    private final UserRepository userRepository; // TODO: 인증 로직 개발 후 제거

    @PostMapping("/rooms/{postId}")
    @Operation(summary = "1대1 채팅방 생성", description = "게시글 작성자와 1대1 채팅방을 생성합니다. 채팅방 이름은 게시글 제목으로 설정됩니다. 이미 채팅방이 존재하면 기존 채팅방을 반환합니다.")
    public ResponseEntity<BaseResponse<ChatRoomResponse>> createChatRoom(
            @Parameter(description = "게시글 ID") @PathVariable Long postId) {

        // TODO: 인증 로직 개발 후 @AuthenticationPrincipal User user로 변경
        User guestUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + postId));

        ChatRoom room = chatRoomService.createNote(post, guestUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success("채팅방 생성 성공", ChatRoomResponse.from(room)));
    }

    @GetMapping("/rooms")
    @Operation(summary = "내 쪽지 채팅방 목록 조회", description = "현재 사용자가 참여한 모든 쪽지 채팅방 목록을 조회합니다.")
    public ResponseEntity<BaseResponse<List<ChatRoomResponse>>> getMyChatRooms() {

        // TODO: 인증 로직 개발 후 @AuthenticationPrincipal User user로 변경
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 유저가 없습니다."));

        List<ChatRoomResponse> rooms = chatRoomService.findMyNoteRooms(user).stream()
                .map(ChatRoomResponse::from)
                .toList();

        return ResponseEntity.ok(BaseResponse.success("채팅방 목록 조회 성공", rooms));
    }
}
