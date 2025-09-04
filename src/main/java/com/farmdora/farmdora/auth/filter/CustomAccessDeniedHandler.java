package com.farmdora.farmdora.auth.filter;

import com.farmdora.farmdora.auth.util.AuthenticationResponseUtil;
import com.farmdora.farmdora.common.error.exception.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("### AccessDeniedHandler : 권한이 없습니다.");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        AuthenticationResponseUtil.authenticateFail(response, HttpStatus.UNAUTHORIZED, ErrorMessage.UNAUTHORIZED.getMessage());
    }
}
