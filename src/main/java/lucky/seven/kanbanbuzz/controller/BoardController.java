package lucky.seven.kanbanbuzz.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<BoardResponseDto>>> findAll() {
        return boardService.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createBoard(@RequestBody BoardRequestDto request) {
        return boardService.createBoard(request);
    }

    @GetMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(@PathVariable Long boardId) {
        return boardService.findOne(boardId);
    }

    @PutMapping({"/{boardId}"})
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(@PathVariable Long boardId,
            @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(boardId, requestDto);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        return boardService.deleteBoard(boardId);
    }

}
