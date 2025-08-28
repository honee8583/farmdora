package com.farmdora.farmdora.auth.auth.register.controller;

import com.farmdora.farmdoraauth.auth.register.dto.UserRegisterDto;
import com.farmdora.farmdoraauth.auth.register.message.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.UserRegisterService;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("${api.prefix}/register")
public class RegisterRestController {
    private final UserRegisterService userRegisterService;
    private static final String EMAIL_SUB = "이메일 인증";
    private static final String EMAIL_TITLE = "이메일 인증 요청";
    private static final String EMAIL_CONTENT = "안녕하세요! 아래의 인증 코드를 일력하여 인증을 완료하세요:";

    @GetMapping("/idcheck")
    public ResponseEntity<?> idCheck(@RequestParam("id") String id) {

        userRegisterService.idCheck(id);
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.ID_CHECK_SUCCESS.getMessage(), true));
    }

    @GetMapping("/emailcheck")
    public ResponseEntity<?> emailCheck(@RequestParam("email") String email) {
        userRegisterService.emailCheck(email);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_CHECK_SUCCESS.getMessage(), true));
    }

    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        log.info(email);
        userRegisterService.sendVerificationEmail(email, EMAIL_SUB, EMAIL_TITLE, EMAIL_CONTENT);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_SEND_SUCCESS.getMessage(), true));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        String code = emailBody.get("code");

        if(userRegisterService.verifyEmail(email, code)){
            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_VERIFY_SUCCESS.getMessage(), true));
        }
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_VERIFY_FAIL.getMessage(), false));
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto userRegisterDto) {

        log.info("요청 파라미터 {}", userRegisterDto);

        boolean result = userRegisterService.registerUser(userRegisterDto);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.USER_REGISTER_SUCCESS.getMessage(), result));

    }
}


