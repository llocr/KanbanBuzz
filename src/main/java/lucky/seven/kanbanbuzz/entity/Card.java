package lucky.seven.kanbanbuzz.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "cards", indexes = {
	@Index(name = "idx_cards_board_id_column_id_user_id", columnList = "board_id, column_id, user_id"),
	@Index(name = "idx_cards_board_id", columnList = "id, board_id")
})

public class Card extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "column_id", nullable = false)
	private Column column;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	
	@jakarta.persistence.Column(nullable = false)
	private String title;
	private String contents;
	private LocalDate endDate;
	
	// 카드에 달린 댓글
	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Comment> comments = new HashSet<>();
}
