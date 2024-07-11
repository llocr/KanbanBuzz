package lucky.seven.kanbanbuzz.repository;

import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    Optional<Column> findByStatusName(String statusName);

    List<ColumnResponseDto> findAllByBoardIdOrderBySortingAsc(Long boardId);

    Long countByBoardId(Long boardId);
}
