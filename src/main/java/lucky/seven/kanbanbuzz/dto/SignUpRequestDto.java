package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @Email
    private String email;

    private String password;

    private String name;

    private boolean manager;
}
