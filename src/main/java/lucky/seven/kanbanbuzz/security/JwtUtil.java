package lucky.seven.kanbanbuzz.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ACCESS_TOKEN_HEADER = "ACCESS_TOKEN_HEADER";
    public static final String REFRESH_TOKEN_HEADER = "REFRESH_TOKEN_HEADER";

    // Access Token 만료시간 설정 (10초)
    public final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L; // 1시간
    // Refresh Token 만료기간 설정(1시간)
    public final long REFRESH_TOKEN_EXPIRATION = 14 * 24 * 60 * 60 * 1000L; //2주

    @Value("${jwt.secret.key}")
    private String secret;
    private Key key;

    // 키 init 하는 메서드 구현해야함
    //    생성자가 만들어진후 한번만 실행됨
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //accessToken과 refreshToken 생성
    public String generateToken(String username,Long expires){
        Date date = new Date();

        return BEARER_PREFIX+
                Jwts.builder().setSubject(username)
                        .setExpiration(new Date(date.getTime()+expires))
                        .setIssuedAt(date)
                        .signWith(SignatureAlgorithm.HS256,secret)
                        .compact();

    }

    public String createAccessToken(String username){
        return generateToken(username,ACCESS_TOKEN_EXPIRATION);
    }

    public String createRefreshToken(String username){
        return generateToken(username,REFRESH_TOKEN_EXPIRATION);
    }

    //accessToken 헤더에서 JWT 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return substringToken(bearerToken);
        }
        return null;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return substringToken(bearerToken);
        }
        return null;
    }
    //JWT 토큰 substring
    public String substringToken(String tokenValue){
        return tokenValue.substring(7);
    }

    //토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(SecurityException | MalformedJwtException e){
            log.error("유효하지 않은 JWT 서명 또는 잘못된 토큰 입니다.");
        }catch (ExpiredJwtException e){
            log.error("만료된 JWT token 입니다.");
        }catch(UnsupportedJwtException e){
            log.error("지원되지 않는 JWT 토큰 입니다.");
        }catch(IllegalArgumentException e){
            log.error("잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 헤더에 AccessToken 담기
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken){
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
    }

}
