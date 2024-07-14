package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardRequestDto {
	@NotNull(message = "컬럼을 선택해주세요.")
	private Long columnId;
	
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	
	private String contents;
	private String endDate;
}
