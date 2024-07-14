package lucky.seven.kanbanbuzz.repository;

import lucky.seven.kanbanbuzz.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    Long countByBoardId(Long boardId);

    @Query("Select c From Column c Where c.board.id = :boardId And c.statusName = :statusName")
    Optional<Column> findByBoardIdAndStatusName(Long boardId, String statusName);

    boolean existsByIdAndBoardId(Long columnId, Long boardId);
}
