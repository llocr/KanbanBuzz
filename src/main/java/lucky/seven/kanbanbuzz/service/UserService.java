package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.SignUpRequestDto;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserRole;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.exception.UserException;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public void signup(SignUpRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Optional<User> checkUser = userRepository.findByEmail(requestDto.getEmail());

        if(checkUser.isPresent()){
            throw new UserException(ErrorType.USER_ALREADY_EXISTS);
        }
        // 사용자 ROLE 확인
        UserRole role = UserRole.ROLE_USER;
        if (requestDto.isManager()) {

            role = UserRole.ROLE_MANAGER;
        }
        User user = User.builder()
                        .email(requestDto.getEmail())
                        .password(encodedPassword)
                        .name(requestDto.getName())
                        .role(role)
                        .build();


        userRepository.save(user);
    }
    @Transactional
    public void updateRefreshToken(String email,String refreshToken){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserException(ErrorType.INVALID_ACCOUNT_ID));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
