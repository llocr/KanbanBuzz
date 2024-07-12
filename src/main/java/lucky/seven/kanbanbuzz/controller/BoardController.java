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
                ResponseMessage.<List<BoardResponseDto>>builder().
                        statusCode(HttpStatus.OK.value()).
                        message("보드 조회 완료").
                        data(list).
                        build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<Long>> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto request) {
        Long boardId = boardService.createBoard(userDetails.getUser(), request);

        ResponseMessage<Long> responseMessage =
                ResponseMessage.<Long>builder().
                        statusCode(HttpStatus.OK.value()).
                        message(boardId + "번째 보드 생성 완료").
                        data(boardId).
                        build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(@PathVariable Long boardId) {
        BoardResponseDto responseDto = boardService.findOne(boardId);

        ResponseMessage<BoardResponseDto> responseMessage =
                ResponseMessage.<BoardResponseDto>builder().
                        statusCode(HttpStatus.OK.value()).
                        message("단일 조회 완료").
                        data(responseDto).
                        build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto) {

        BoardResponseDto responseDto = boardService.updateBoard(userDetails.getUser(), boardId,
                requestDto);

        ResponseMessage<BoardResponseDto> responseMessage =
                ResponseMessage.<BoardResponseDto>builder().
                        statusCode(HttpStatus.OK.value()).
                        message(boardId + "번째 보드 수정 완료").
                        data(responseDto).
                        build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseMessage<Long>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId) {
        Long deletedBoardId = boardService.deleteBoard(userDetails.getUser(), boardId);

        ResponseMessage<Long> responseMessage =
                ResponseMessage.<Long>builder().
                statusCode(HttpStatus.OK.value()).
                message(deletedBoardId + "번째 보드 삭제 완료").
                data(deletedBoardId).
                build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping("/{boardId}/invitation")
    public ResponseEntity<ResponseMessage<Long>> inviteUser
            (@AuthenticationPrincipal UserDetailsImpl userDetails
                    , @PathVariable Long boardId,
                    @RequestParam String userEmail) {
        Long invitedBoardId = boardService.inviteUser(userDetails.getUser(), boardId, userEmail);

        ResponseMessage<Long> responseMessage =
                ResponseMessage.<Long>builder().
                statusCode(HttpStatus.OK.value()).
                message(invitedBoardId + "번째 보드에" + userEmail + " 회원 초대를 완료하였습니다.").
                data(invitedBoardId).
                build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
