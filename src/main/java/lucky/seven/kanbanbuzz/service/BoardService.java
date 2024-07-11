package lucky.seven.kanbanbuzz.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.entity.Board;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserBoard;
import lucky.seven.kanbanbuzz.entity.UserRole;
import lucky.seven.kanbanbuzz.exception.BoardException;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.repository.BoardRepository;
import lucky.seven.kanbanbuzz.repository.UserBoardRepository;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final UserRepository userRepository;

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

    public ResponseEntity<String> createBoard(User user, BoardRequestDto request) {
        if(user.getRole() == UserRole.ROLE_USER){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("글을 작성 할 권한이 없습니다.");
        }
        boardRepository.save(Board.builder().requestDto(request).build());
        return ResponseEntity.status(HttpStatus.OK).body("등록 완료");
    }


    public ResponseEntity<ResponseMessage<BoardResponseDto>> findOne(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        BoardResponseDto responseDto = BoardResponseDto.from(board);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseMessage.<BoardResponseDto>builder().
                        statusCode(200).
                        message("단일 조회 완료").
                        data(responseDto).build());
    }

    @Transactional
    public ResponseEntity<ResponseMessage<BoardResponseDto>> updateBoard(User user, Long id,
            BoardRequestDto requestDto) {

        
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        if(user.getRole() == UserRole.ROLE_USER){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseMessage.<BoardResponseDto>builder().
                            statusCode(401).
                            message("글을 수정 할 권한이 없습니다.")
                            .build()
                    );
        }

        board.update(requestDto);
        BoardResponseDto responseDto = BoardResponseDto.from(board);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.<BoardResponseDto>builder().
                        statusCode(200).
                        message("수정 완료").
                        data(responseDto).build());
    }

    @Transactional
    public ResponseEntity<String> deleteBoard(User user , Long id) {

        if(user.getRole() == UserRole.ROLE_USER){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("삭제 할 권한이 없습니다.");
        }

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));

        boardRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(id + "번째 board 삭제 완료.");
    }

    @Transactional
    public ResponseEntity<String> inviteUser(User user, Long id, String userEmail) {

        if(user.getRole() == UserRole.ROLE_USER){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("초대 권한이 없습니다.");
        }

        Board board = boardRepository.findById(id).orElseThrow();
        Optional<UserBoard> userBoardOptional = userBoardRepository.findByUserEmail(userEmail);

        // 이미 초대 된 사용자를 초대 한 경우
        if (userBoardOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 사용자는 이미 초대 되어 있습니다.");
        } else {
            User invitedUser = userRepository.findByEmail(userEmail).orElseThrow(
                    () -> new BoardException(ErrorType.NOT_FOUND_USER)
            );

            // 해당 유저 role 변경 후 저장
            User changedUser = User.builder().email(invitedUser.getEmail())
                    .password(invitedUser.getPassword())
                    .name(invitedUser.getName())
                    .refreshToken(invitedUser.getRefreshToken())
                    .role(UserRole.ROLE_MANAGER)
                    .build();

            UserBoard userBoard = UserBoard.builder()
                    .user(changedUser).board(board).build();

            userBoardRepository.save(userBoard);
            return ResponseEntity.status(HttpStatus.OK).body(userEmail + " 사용자를 초대 완료하였습니다.");
        }

    }
}
