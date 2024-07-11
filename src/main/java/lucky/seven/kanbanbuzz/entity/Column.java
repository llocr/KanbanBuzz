package lucky.seven.kanbanbuzz.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lucky.seven.kanbanbuzz.dto.ColumnRequestDto;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "columns")
@NoArgsConstructor
@Data
public class Column {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String statusName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	// 컬럼에 속한 카드
	@OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Card> cards = new HashSet<>();

	private Long sorting;

	@Builder
	public Column(String statusName, Board board, Set<Card> cards, Long sorting) {
		this.statusName = statusName;
		this.board = board;
		this.cards = cards;
		this.sorting = sorting;
	}

	@Builder
	public static Column saveColumn(Board board, ColumnRequestDto requestDto, Long sort) {
		return Column.builder()
				.statusName(requestDto.getStatusName())
				.board(board)
				.sorting(sort)
				.build();
	}

	public void reorderColumn(Long count) {
		this.sorting = count;
	}
}
