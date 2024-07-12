package lucky.seven.kanbanbuzz.repository;

import java.util.Optional;
import lucky.seven.kanbanbuzz.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {

    @Query("SELECT ub FROM UserBoard ub WHERE ub.user.email = :email AND ub.board.id = :boardId")
    Optional<UserBoard> findByUserEmailAndBoardId(@Param("email") String email, @Param("boardId") Long boardId);

}
