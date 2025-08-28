package com.farmdora.farmdora.config;

import com.farmdora.farmdoraauth.auth.oauth.handler.CustomOAuth2FailureHandler;
import com.farmdora.farmdoraauth.auth.oauth.handler.CustomOAuth2SuccessHandler;
import com.farmdora.farmdoraauth.auth.oauth.service.CustomOAuth2UserService;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.jwt.JwtAuthenticationFilter;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import com.farmdora.farmdoraauth.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    ////AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler oAuth2FailureHandler;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 필드의 configuration 안 쓰고 파라미터로
    // AuthenticationManager는 UserDetailsService 기반으로 자동 구성됨
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisTemplate, userRepository);
        loginFilter.setFilterProcessesUrl("/api/auth/login"); // ⭐️ 로그인 경로 변경

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/find/**").permitAll()
                        .requestMatchers("/api/auth/login/**").permitAll()
                        .requestMatchers("/api/auth/login/oauth2/code/**").permitAll()
                        .requestMatchers("/api/auth/login/logout").hasAnyRole("USER", "ADMIN", "SELLER")
                        .requestMatchers("/api/auth/oauth/id/save").hasAnyRole("USER", "ADMIN", "SELLER")
                        .requestMatchers("/api/auth/auth/register/**").permitAll()
                        .requestMatchers("/api/auth/mypage/admin/user/**").hasRole("ADMIN")
                        .requestMatchers("/api/auth/mypage/user/**").hasRole("USER")
                        .anyRequest().permitAll())
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)  // 수정된 loginFilter 등록
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth->auth.baseUri("/api/auth/oauth2/authorization/**"))
                        .redirectionEndpoint(auth -> auth.baseUri("/api/auth/login/oauth2/code/{registrationId}"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://farm-dora.kro.kr",
                "http://www.farm-dora.kro.kr",
                "https://www.farm-dora.kro.kr"
        )); // 허용할 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // 모든 요청 헤더를 수락한다.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
