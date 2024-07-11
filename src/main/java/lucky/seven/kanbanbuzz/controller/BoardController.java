package lucky.seven.kanbanbuzz.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.repository.BoardRepository;
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
    private final BoardRepository boardRepository;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<BoardResponseDto>>> findAll() {

        List<BoardResponseDto> list = boardRepository.findAll()
                .stream()
                .map(BoardResponseDto::from).toList();

        ResponseMessage<List<BoardResponseDto>> responseMessage =
                ResponseMessage.<List<BoardResponseDto>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("가게 조회 완료")
                        .data(list)
                        .build();
        return boardService.findAll(responseMessage);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<String>> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto request) {
        return boardService.createBoard(userDetails.getUser(), request);
    }

    @GetMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(@PathVariable Long boardId) {
        return boardService.findOne(boardId);
    }

    @PutMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(userDetails.getUser(), boardId, requestDto);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseMessage<String>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId) {
        return boardService.deleteBoard(userDetails.getUser(), boardId);
    }

    @PostMapping("/{boardId}/invitation")
    public ResponseEntity<String> inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @PathVariable Long boardId,
            @RequestParam String userEmail) {
        return boardService.inviteUser(userDetails.getUser(), boardId, userEmail);
    }
}
