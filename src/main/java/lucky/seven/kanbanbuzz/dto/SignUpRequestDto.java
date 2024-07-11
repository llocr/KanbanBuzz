package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
            message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    private boolean manager = false;
}
