package lucky.seven.kanbanbuzz.dto;

import lombok.Getter;
import lucky.seven.kanbanbuzz.entity.Card;

@Getter
public class CardSimpleResponseDto {
	private Long columnId;
	private String title;
	private String user;
	
	public CardSimpleResponseDto(Card card) {
		this.columnId = card.getColumn().getId();
		this.title = card.getTitle();
		this.user = card.getUser().getName();
	}
}
