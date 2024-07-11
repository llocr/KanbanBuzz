package lucky.seven.kanbanbuzz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.dto.SignUpRequestDto;
import lucky.seven.kanbanbuzz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<SignUpRequestDto>> signup(@Validated @RequestBody SignUpRequestDto requestDto) {
        userService.signup(requestDto);
        ResponseMessage<SignUpRequestDto> responseMessage = ResponseMessage.<SignUpRequestDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(requestDto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }
}
