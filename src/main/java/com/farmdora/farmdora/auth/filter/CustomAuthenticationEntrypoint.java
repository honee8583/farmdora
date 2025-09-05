package com.farmdora.farmdora.auth.filter;

import com.farmdora.farmdora.auth.util.AuthenticationResponseUtil;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException, ServletException {

        log.error("### AuthenticationEntrypoint : 인증이 필요합니다.");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        AuthenticationResponseUtil.authenticateFail(response, HttpStatus.UNAUTHORIZED, ErrorMessage.UNAUTHORIZED.getMessage());
    }
}
