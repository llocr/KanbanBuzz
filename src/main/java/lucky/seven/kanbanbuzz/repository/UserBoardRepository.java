package lucky.seven.kanbanbuzz.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import lucky.seven.kanbanbuzz.entity.UserBoard;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
	Optional<UserBoard> findByBoardIdAndUserId(Long boardId, Long id);
	
	Set<UserBoard> findByBoardId(Long boardId);
}
