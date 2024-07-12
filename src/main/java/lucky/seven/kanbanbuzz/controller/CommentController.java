package lucky.seven.kanbanbuzz.controller;

import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.CommentRequestDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.security.UserDetailsImpl;
import lucky.seven.kanbanbuzz.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<ResponseMessage<Object>> createComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto requestDto
    ){
        commentService.createComment(userDetails.getUser(),cardId,requestDto);
        ResponseMessage<Object> message = ResponseMessage.builder().message("댓글 달기 완료").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}
