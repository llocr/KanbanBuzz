package lucky.seven.kanbanbuzz.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lucky.seven.kanbanbuzz.entity.Column;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
	Set<Column> findByBoardId(Long boardId);
	
	@Query("SELECT c FROM Column c WHERE c.board.id = :boardId AND c.id = :columnId")
	Optional<Column> findColumnByIdAndBoard(Long boardId, Long columnId);
}
