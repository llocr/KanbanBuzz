package lucky.seven.kanbanbuzz.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
  Set<Column> findByBoardId(Long boardId);

  @Query("SELECT c FROM Column c WHERE c.board.id = :boardId AND c.id = :columnId")
  Optional<Column> findColumnByIdAndBoard(Long boardId, Long columnId);

    Long countByBoardId(Long boardId);
  List<ColumnResponseDto> findAllByBoardIdOrderBySortingAsc(Long boardId);

  Long countByBoardId(Long boardId);

    @Query("Select c From Column c Where c.board.id = :boardId And c.statusName = :statusName")
    Optional<Column> findByBoardIdAndStatusName(Long boardId, String statusName);

    boolean existsByIdAndBoardId(Long columnId, Long boardId);
  Optional<Column> findByBoardIdAndStatusName(Long boardId, String statusName);

  Optional<Object> findByIdAndBoardId(Long columnId, Long boardId);

}
