package lucky.seven.kanbanbuzz.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j(topic = "로그아웃 필터")
public class LogoutFilter extends OncePerRequestFilter {
    private final UserService userService;

    public LogoutFilter(UserService userService) {
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/api/user/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth!=null){
                new SecurityContextLogoutHandler().logout(request,response,auth);
            }
            //헤더에서 토큰 삭제
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER,"");

            //user 엔티티에서 refresh 토큰 제거
            String email = request.getHeader("email");
            userService.updateRefreshToken(email,null);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("로그아웃 성공");
            return;
        }
        filterChain.doFilter(request,response);

    }
}
