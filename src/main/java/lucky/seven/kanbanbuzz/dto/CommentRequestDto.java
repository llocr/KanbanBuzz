package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank(message = "댓글이 입력되지 않았습니다.")
    private String contents;
}
