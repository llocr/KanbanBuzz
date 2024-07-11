package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ColumnRequestDto {
    @NotBlank(message = "컬럼 상태가 입력되지 않았습니다.")
    String statusName;
}
