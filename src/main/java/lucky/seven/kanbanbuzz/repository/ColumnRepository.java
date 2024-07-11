package lucky.seven.kanbanbuzz.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lucky.seven.kanbanbuzz.entity.Column;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
	Set<Column> findByBoardId(Long boardId);
}
