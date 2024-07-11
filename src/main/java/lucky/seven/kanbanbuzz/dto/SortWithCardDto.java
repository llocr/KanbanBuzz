package lucky.seven.kanbanbuzz.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SortWithCardDto {
	private Long SortId;
	private String SortName;
	private List<CardSimpleResponseDto> cards;
}
