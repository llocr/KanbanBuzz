package lucky.seven.kanbanbuzz.dto;

import lombok.Getter;

@Getter
public class CardUpdateDto {
	private Long columnId;
	private String title;
	private String contents;
	private String endDate;
}
