package lucky.seven.kanbanbuzz.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	private String refreshToken;
	
	//유저가 매니저인 보드
	@OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Board> managerBoards = new HashSet<>();
	
	//유저가 속한 보드
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserBoard> userBoards = new HashSet<>();
	
	//유저가 작업할 카드
	@OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Card> userCards = new HashSet<>();
	
	//유저가 작성한 댓글
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private Set<Comment> comments = new HashSet<>();
}
