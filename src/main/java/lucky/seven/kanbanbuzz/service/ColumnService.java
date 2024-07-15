package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.CardRequestDto;
import lucky.seven.kanbanbuzz.dto.ColumnRequestDto;
import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.entity.Board;
import lucky.seven.kanbanbuzz.entity.Column;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserRole;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.exception.UserException;
import lucky.seven.kanbanbuzz.repository.BoardRepository;
import lucky.seven.kanbanbuzz.repository.ColumnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final CardService cardService;

    /**
     * 컬럼 생성
     * @param boardId
     * @param requestDto
     * @return
     */
    @Transactional
    public ColumnResponseDto addColumn(Long boardId, ColumnRequestDto requestDto, User user) {

        checkUser(user);
        Board findBoard = findBoardById(boardId);

        if(columnRepository.findByBoardIdAndStatusName(boardId, requestDto.getStatusName()).isPresent()) {
            throw new UserException(ErrorType.DUPLICATE_STATUS_NAME);
        }

        Long sort = columnRepository.countByBoardId(boardId) + 1L;
        Column column = Column.saveColumn(findBoard, requestDto, sort);
        columnRepository.save(column);

        CardRequestDto cardRequestDto = new CardRequestDto(column.getId(), "title", "", "2000-01-01");
        cardService.saveCard(boardId, cardRequestDto, user);

        return ColumnResponseDto.of(column);
    }

    /**
     * 컬럼 삭제
     * @param boardId
     * @param columnId
     */
    @Transactional
    public void deleteColumn(Long boardId, Long columnId, User user) {

        checkUser(user);

        if(!columnRepository.existsByIdAndBoardId(columnId, boardId)) {
            throw new UserException(ErrorType.INVALID_COLUMN);
        }

        columnRepository.deleteById(columnId);
    }

    /**
     * 컬럼 재정렬
     * @param boardId
     * @param id
     */
    @Transactional
    public void reorderColumns(Long boardId, Long[] id, User user) {

        checkUser(user);
        Board findBoard = findBoardById(boardId);

        Long count = 1L;
        Column column;

        for(Long i : id) {
            column = columnRepository.getReferenceById(i);
            column.reorderColumn(count);
            count++;
        }
    }


    /**
     * 해당 아이디를 가진 보드가 존재하는지 확인
     * @param boardId
     * @return
     */
    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new UserException(ErrorType.NOT_FOUND_BOARD));
    }

    /**
     * 사용자가 manager인지 확인
     * @param user
     */
    private void checkUser(User user) {
        if(!user.getRole().equals(UserRole.ROLE_MANAGER)) {
            throw new UserException(ErrorType.NOT_AUTHORIZED_BOARD);
        }
    }
}
