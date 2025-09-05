package com.farmdora.farmdora.auth.filter;

import com.farmdora.farmdora.auth.dto.JwtConstants;
import com.farmdora.farmdora.auth.dto.LoginUser;
import com.farmdora.farmdora.auth.service.LoginService;
import com.farmdora.farmdora.auth.util.AuthenticationResponseUtil;
import com.farmdora.farmdora.auth.util.JwtUtil;
import com.farmdora.farmdora.common.error.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    // 아래 url들은 jwt 토큰 검사를 패스함
    private static final List<String> WHITELIST = List.of(
            "/api/auth/login",
            "/api/auth/join"
    );
    private static final AntPathMatcher PATH = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 브라우저가 CORS 확인할 때 OPTIONS 요청을 먼저 보내는데, 이건 JWT 검증 같은 인증 절차를 거치면 안됨
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        log.info("### 화이트 리스트를 확인합니다...");

        // true 리턴시 doFilterInternal을 거치지 않음.
        String uri = request.getRequestURI();
        return WHITELIST.stream().anyMatch(p -> PATH.match(p, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        log.info("### JWT를 체크합니다...");

        String token = resolveBearerToken(req);
        if (!StringUtils.hasText(token)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            if (StringUtils.hasText(token)) {
                LoginUser principal = jwtUtil.verify(token);
                principal = loginService.getLoginUser(principal.getId());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities());

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (CustomException e) {
            log.error("### 인증도중 에러가 발생했습니다.");

            // JWT토큰이 만료, 유효하지 않은 경우
            res.setStatus(e.getStatus().value());
            AuthenticationResponseUtil.authenticateFail(res, e.getStatus(), e.getMessage());
            return;
        }

        chain.doFilter(req, res);
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header)) {
            log.error("### Authorization 헤더가 존재하지 않습니다.");
            return null;
        }

        if (!header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            log.error("### Bearer 토큰이 존재하지 않습니다.");
            return null;
        }
        return header.substring(JwtConstants.TOKEN_PREFIX.length()).trim();
    }
}
