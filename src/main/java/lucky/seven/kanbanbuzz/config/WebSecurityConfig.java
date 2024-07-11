package lucky.seven.kanbanbuzz.config;


import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.repository.UserRepository;
import lucky.seven.kanbanbuzz.security.*;
import lucky.seven.kanbanbuzz.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration, UserRepository userRepository,@Lazy UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final UserService userService;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(jwtUtil, userRepository);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public Filter authorizationFilter(){
        return new AuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public LogoutFilter logoutFilter(UserService userService,JwtUtil jwtUtil){
        return new LogoutFilter(userService,jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        // jwt 사용 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .anyRequest().authenticated()
        );
        http.addFilterBefore(logoutFilter(userService,jwtUtil),AuthenticationFilter.class);
        http.addFilterBefore(authorizationFilter(), AuthenticationFilter.class);
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}