package lucky.seven.kanbanbuzz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String email;
    private String password;
    private String name;
    private boolean manager = false;
}
