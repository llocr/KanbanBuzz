package lucky.seven.kanbanbuzz.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;
import lucky.seven.kanbanbuzz.dto.BoardResponseDto;
import lucky.seven.kanbanbuzz.entity.Board;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserBoard;
import lucky.seven.kanbanbuzz.entity.UserRole;
import lucky.seven.kanbanbuzz.exception.BoardException;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.repository.BoardRepository;
import lucky.seven.kanbanbuzz.repository.UserBoardRepository;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final UserRepository userRepository;


    public List<BoardResponseDto> findAll() {

        return boardRepository.findAll()
                .stream()
                .map(BoardResponseDto::from).toList();

    }

    public Long createBoard(User user, BoardRequestDto request) {
        checkRole(user);
        Board board = Board.builder().requestDto(request).build();
        boardRepository.save(board);

        return board.getId();
    }

    public BoardResponseDto findOne(Long id) {
        Board board = getBoardById(id);
        return BoardResponseDto.from(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(User user, Long id,
            BoardRequestDto requestDto) {
        checkRole(user);
        Board board = getBoardById(id);
        board.update(requestDto);
        return BoardResponseDto.from(board);
    }

    @Transactional
    public Long deleteBoard(User user, Long id) {
        checkRole(user);
        Board board = getBoardById(id);
        boardRepository.deleteById(id);
        return id;
    }


    @Transactional
    public Long inviteUser(User user, Long id, String userEmail) {

        Optional<UserBoard> userBoardOptional = userBoardRepository.findByUserEmailAndBoardId(
                userEmail, id);

        // 이미 초대 된 사용자를 초대 한 경우
        if (userBoardOptional.isPresent()) {
            throw new BoardException(ErrorType.USER_ALREADY_EXISTS);
        }

        checkRole(user);
        Board board = getBoardById(id);

        User invitedUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new BoardException(ErrorType.NOT_FOUND_USER)
        );

        UserBoard userBoard = UserBoard.builder().
                user(invitedUser).
                board(board).
                build();

        userBoardRepository.save(userBoard);
        return id;
    }

    // 유저 권한 조회 메서드
    public void checkRole(User user) {
        if (user.getRole() == UserRole.ROLE_USER) {
            throw new BoardException(ErrorType.NOT_AUTHORIZED_BOARD);
        }
    }

    // 보드 탐색 메서드
    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorType.NOT_FOUND_BOARD));
    }

}
