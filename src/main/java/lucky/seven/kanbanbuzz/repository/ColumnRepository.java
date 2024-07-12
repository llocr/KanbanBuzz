package lucky.seven.kanbanbuzz.repository;

import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<ColumnResponseDto> findAllByBoardIdOrderBySortingAsc(Long boardId);

    Long countByBoardId(Long boardId);

    Optional<Column> findByBoardIdAndStatusName(Long boardId, String statusName);

    Optional<Object> findByIdAndBoardId(Long columnId, Long boardId);
}
