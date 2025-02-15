package lucky.seven.kanbanbuzz.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lucky.seven.kanbanbuzz.dto.BoardRequestDto;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boards", indexes = {
        @Index(name = "idx_board_name", columnList = "name")
})
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String bio;

    // 보드에 속한 유저
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBoard> userBoards = new HashSet<>();

    // 보드에 속한 컬럼
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Column> columns = new HashSet<>();

    //보드에 속한 카드
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    @Builder
    public Board(BoardRequestDto requestDto) {
        this.name = requestDto.getName();
        this.bio = requestDto.getBio();
    }

    public void update(BoardRequestDto requestDto) {
        this.name = requestDto.getName();
        this.bio = requestDto.getBio();
    }
}
