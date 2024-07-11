package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

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

        if(findColumn(boardId, requestDto.getStatusName())) {
            throw new UserException(ErrorType.DUPLICATE_STATUS_NAME);
        }

        Long sort = columnRepository.countByBoardId(boardId) + 1L;

        Column column = Column.saveColumn(findBoard, requestDto, sort);
        columnRepository.save(column);

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
        Board findBoard = findBoardById(boardId);

        if(columnRepository.findById(columnId).isEmpty()) {
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
     * 특정 보드의 컬럼을 정렬하여 반환
     * @param boardId
     * @return
     */
    @Transactional
    public List<ColumnResponseDto> queryColumns(Long boardId) {

        Board findBoard = findBoardById(boardId);
        return columnRepository.findAllByBoardIdOrderBySortingAsc(boardId);
    }

    /**
     * 입력한 컬럼 이름이 같은 보드에 존재하는지 확인
     * @param boardId
     * @param statusName
     * @return
     */
    private boolean findColumn(Long boardId, String statusName) {
        List<Column> columns = columnRepository.findByStatusName(statusName);

        if(columns.isEmpty()) {
            return false;
        }

        for(Column column : columns) {
            if(column.getBoard().getId().equals(boardId)) {
                return true;
            }
        }
        return false;
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
