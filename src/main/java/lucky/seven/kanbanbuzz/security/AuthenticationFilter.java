package lucky.seven.kanbanbuzz.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.ErrorMessage;
import lucky.seven.kanbanbuzz.dto.LoginRequestDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    public AuthenticationFilter(JwtUtil jwtUtil, UserRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공시 처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();


        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        //UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken(email);

        user.setRefreshToken(refreshToken.substring(7));
        repository.save(user);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseMessage.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인 성공")
                .data(email)
                .build())
        );
        response.getWriter().flush();
    }

    // 로그인 실패시 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorType errorType = ErrorType.NOT_FOUND_AUTHENTICATION_INFO;
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ErrorMessage.<String>builder()
                .statusCode(errorType.getHttpStatus().value())
                .message(errorType.getMessage())
                .build())
        );
        response.getWriter().flush();
    }
}