package lucky.seven.kanbanbuzz.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import lucky.seven.kanbanbuzz.entity.UserBoard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
	Optional<UserBoard> findByBoardIdAndUserId(Long boardId, Long id);
	
	Set<UserBoard> findByBoardId(Long boardId);
    @Query("SELECT ub FROM UserBoard ub WHERE ub.user.email = :email AND ub.board.id = :boardId")
    Optional<UserBoard> findByUserEmailAndBoardId(@Param("email") String email, @Param("boardId") Long boardId);

    @Query("SELECT ub FROM UserBoard ub WHERE ub.user.email = :email")
    Optional<UserBoard> findByUserEmail(@Param("email") String email);
}
