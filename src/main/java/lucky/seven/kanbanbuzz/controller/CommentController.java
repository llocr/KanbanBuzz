package lucky.seven.kanbanbuzz.controller;

import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.CommentRequestDto;
import lucky.seven.kanbanbuzz.dto.CommentResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.security.UserDetailsImpl;
import lucky.seven.kanbanbuzz.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        ResponseMessage<Object> responseMessage = ResponseMessage.builder().message("댓글 달기 완료").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/cards/{cardId}/comments")
    public ResponseEntity<ResponseMessage<Object>>  getComments(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ){
        Pageable pageable = (Pageable) PageRequest.of(page,size, Sort.by(sortBy).descending());
        Page<CommentResponseDto> pages = commentService.getComments(userDetails.getUser(),cardId,pageable);
        ResponseMessage<Object> responseMessage = ResponseMessage
                .builder().data(pages)
                .message("댓글 조회 완료")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
