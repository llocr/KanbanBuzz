package lucky.seven.kanbanbuzz.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments", indexes = {
		@Index(name = "idx_comment_card", columnList = "card_id")
})
@NoArgsConstructor
public class Comment extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id", nullable = false)
	private Card card;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	private String contents;
	@Builder
	public Comment(Card card, User user, String contents) {
		this.card = card;
		this.user = user;
		this.contents = contents;
	}
}
