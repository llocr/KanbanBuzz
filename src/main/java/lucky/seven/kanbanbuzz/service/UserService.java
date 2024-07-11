package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.SignUpRequestDto;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserRole;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public void signup(SignUpRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setName(requestDto.getName());

        // 사용자 ROLE 확인
        UserRole role = UserRole.ROLE_USER;
        if (requestDto.isManager()) {

            role = UserRole.ROLE_MANAGER;
        }
        user.setRole(role);
        repository.save(user);
    }
}
