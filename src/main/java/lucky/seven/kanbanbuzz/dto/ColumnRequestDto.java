package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ColumnRequestDto {
    @NotBlank(message = "컬럼 상태가 입력되지 않았습니다.")
    private String statusName;
}
