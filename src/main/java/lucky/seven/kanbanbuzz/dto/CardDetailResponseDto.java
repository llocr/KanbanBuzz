package lucky.seven.kanbanbuzz.dto;

import lombok.Getter;
import lucky.seven.kanbanbuzz.entity.Card;

@Getter
public class CardDetailResponseDto {
	private String title;
	private String endDate;
	private String contents;
	private String user;
	
	public CardDetailResponseDto(Card card) {
		this.title = card.getTitle();
		this.endDate = card.getEndDate() == null ? "없음" : card.getEndDate().toString();
		this.contents = card.getContents() == null ? "없음" : card.getContents();
		this.user = card.getUser().getName();
	}
}
