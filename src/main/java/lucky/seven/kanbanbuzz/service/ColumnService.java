package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.ColumnRequestDto;
import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.entity.Column;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.exception.UserException;
import lucky.seven.kanbanbuzz.repository.ColumnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
//    private final BoardRepository boardRepository;

    /**
     * 컬럼 생성
     * @param boardId
     * @param requestDto
     * @return
     */
    @Transactional
    public ColumnResponseDto addColumn(Long boardId, ColumnRequestDto requestDto) {

        if(findColumn(boardId, requestDto.getStatusName())) {
            throw new UserException(ErrorType.DUPLICATE_STATUS_NAME);
        }

        Long sort = columnRepository.countByBoardId(boardId) + 1L;

        Column column = Column.saveColumn(requestDto, sort);
        columnRepository.save(column);

        return ColumnResponseDto.of(column);
    }

    /**
     * 컬럼 삭제
     * @param boardId
     * @param columnId
     */
    @Transactional
    public void deleteColumn(Long boardId, Long columnId) {
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
    public void reorderColumns(Long boardId, Long[] id) {
        Long count = 1L;
        Column column;

        for(Long i : id) {
            column = columnRepository.getReferenceById(i);
            column.reorderColumn(count);
            count++;
        }
    }

    /**
     * 정렬된 컬럼 반환
     * @param boardId
     * @return
     */
    public List<ColumnResponseDto> queryColumns(Long boardId) {
        return columnRepository.findAllByBoardIdOrderBySortingAsc(boardId);
    }

    /**
     * 입력한 컬럼 이름이 같은 보드에 존재하는지 확인
     * @param boardId
     * @param statusName
     * @return
     */
    private boolean findColumn(Long boardId, String statusName) {
        Column column = columnRepository.findByStatusName(statusName)
                .orElseThrow(() -> new UserException(ErrorType.INVALID_COLUMN));

        return column.getBoard().getId().equals(boardId);
    }

//    private Board findBoard(Long boardId) {
//        return boardRepository.findById(boardId)
//                .orElseThrow(() -> new UserException(ErrorType.DUPLICATE_STATUS_NAME));
//    }

//    private void findAndCheckUser(User user) {
//
//        if(!user.getUserStatus().equals("MANAGER")) {
//            throw new UserException(ErrorType.DUPLICATE_ACCOUNT_ID);
//        }
//    }
}
