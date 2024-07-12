package lucky.seven.kanbanbuzz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lucky.seven.kanbanbuzz.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	@Query("SELECT c FROM Card c JOIN FETCH c.column JOIN FETCH c.user WHERE c.board.id = :boardId")
	List<Card> findByBoardIdWithColumnAndUser(@Param("boardId") Long boardId);
	
	@Query("SELECT c FROM Card c JOIN FETCH c.user JOIN FETCH c.board JOIN FETCH c.column WHERE c.id = :cardId AND c.board.id = :boardId")
	Optional<Card> findByIdWithUserBoardAndColumn(@Param("cardId") Long cardId, @Param("boardId") Long boardId);
	
	Optional<Card> findByIdAndUserId(Long cardId, Long id);
}
