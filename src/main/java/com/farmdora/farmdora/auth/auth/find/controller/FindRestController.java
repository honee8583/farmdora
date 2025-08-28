package com.farmdora.farmdora.auth.auth.find.controller;

import com.farmdora.farmdoraauth.auth.exception.EmailException;
import com.farmdora.farmdoraauth.auth.find.dto.FindDto;
import com.farmdora.farmdoraauth.auth.find.dto.SendDto;
import com.farmdora.farmdoraauth.auth.find.message.FindMessage;
import com.farmdora.farmdoraauth.auth.find.service.FindService;
import com.farmdora.farmdoraauth.auth.register.message.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.UserRegisterService;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/find")
public class FindRestController {

    private final FindService findService;
    private final UserRegisterService userRegisterService;

    @PostMapping("/send/code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SendDto request) {
        String email = request.getEmail();
        boolean isEmail = findService.existEmail(email, request.getId(), request.getName());
        if (isEmail) {
            userRegisterService.sendVerificationEmail(email, FindMessage.EMAIL_SUB.getMessage(), FindMessage.EMAIL_TITLE.getMessage(), FindMessage.EMAIL_CONTENT.getMessage());

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_SEND_SUCCESS.getMessage(), true));
        }else {
            throw new ResourceNotFoundException("아이디, 비번 찾기",email);
        }
    }

    @PostMapping("/send/value")
    public ResponseEntity<?> sendValue(@RequestBody FindDto requestBody) {

        if (findService.sendFind(requestBody.getEmail(), requestBody.getCode(), requestBody.getFind())){
            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.EMAIL_VERIFY_SUCCESS.getMessage(), true));
        }else {
            throw new EmailException("이메일 인증 실패",HttpStatus.NOT_FOUND);
        }
    }
}
