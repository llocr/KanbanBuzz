package lucky.seven.kanbanbuzz.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.entity.Board;
import lucky.seven.kanbanbuzz.exception.BoardException;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.repository.BoardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //TODO
    // 공통적으로 나중에 User 검증 추가
    // User Role에 따른 로직 추가
    // 예외처리

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
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    public ResponseEntity<String> createBoard(BoardRequestDto request) {
        boardRepository.save(Board.builder().requestDto(request).build());
        return ResponseEntity.status(HttpStatus.OK).body("등록 완료");
    }


    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        //if(board == null) 예외처리

        BoardResponseDto responseDto = BoardResponseDto.from(board);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<BoardResponseDto>builder().data(responseDto).build());
    }

    @Transactional
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(Long id,
            BoardRequestDto requestDto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        board.update(requestDto);
        BoardResponseDto responseDto = BoardResponseDto.from(board);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.<BoardResponseDto>builder().data(responseDto).build());
    }

    @Transactional
    public ResponseEntity<String> deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        boardRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(id + "번째 board 삭제 완료.");
    }
}
