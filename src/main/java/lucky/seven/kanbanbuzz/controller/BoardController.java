package lucky.seven.kanbanbuzz.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.security.UserDetailsImpl;
import lucky.seven.kanbanbuzz.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<BoardResponseDto>>> findAll() {

        List<BoardResponseDto> list = boardService.findAll();

        ResponseMessage<List<BoardResponseDto>> responseMessage =
                ResponseMessage.<List<BoardResponseDto>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("가게 조회 완료")
                        .data(list)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<String>> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto request) {
        String message = boardService.createBoard(userDetails.getUser(), request);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<String>builder().
                        statusCode(200).
                        message(message).
                        build()
        );
    }

    @GetMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(@PathVariable Long boardId) {
        BoardResponseDto responseDto = boardService.findOne(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<BoardResponseDto>builder().
                        statusCode(200).
                        message("단일 조회 완료").
                        data(responseDto).
                        build());
    }

    @PutMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto) {

        BoardResponseDto responseDto = boardService.updateBoard(userDetails.getUser(), boardId,
                requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.<BoardResponseDto>builder().
                        statusCode(200).
                        message("수정 완료").
                        data(responseDto).build());
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseMessage<String>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId) {
        String message = boardService.deleteBoard(userDetails.getUser(), boardId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<String>builder().
                        statusCode(200).
                        message(message).
                        build()
        );
    }

    @PostMapping("/{boardId}/invitation")
    public ResponseEntity<ResponseMessage<String>> inviteUser
            (@AuthenticationPrincipal UserDetailsImpl userDetails
                    , @PathVariable Long boardId,
                    @RequestParam String userEmail) {
        String message = boardService.inviteUser(userDetails.getUser(), boardId, userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<String>builder().
                        statusCode(200).
                        message(message).
                        build()
        );
    }
}
