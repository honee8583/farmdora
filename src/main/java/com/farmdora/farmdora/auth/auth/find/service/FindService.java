package com.farmdora.farmdora.auth.auth.find.service;

import com.farmdora.farmdoraauth.auth.find.findenum.FindType;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.auth.register.service.EmailSendService;
import com.farmdora.farmdoraauth.auth.register.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FindService {

    private final EmailSendService emailSendService;
    private final UserRepository userRepository;
    private final UserRegisterService userRegisterService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public boolean existEmail(String email, String id, String name) {

        if (id == null || id.isEmpty()) {
            return userRepository.existsUserByEmailAndName(email,name);
        } else {
            return userRepository.existsUserByEmailAndId(email, id);
        }
    }

    public boolean sendFind(String email, String code, FindType findType) {
        String sub = findType.getSubject();
        String emailBody = getEmailBody(findType, email); // 분리된 메서드 호출
        log.info("service sendFind() email: {}", email);
        if (userRegisterService.verifyEmail(email, code)) {
            emailSendService.sendEmail(email, sub, emailBody, findType.getTitle(), findType.getContent());
            return true;
        } else {
            log.error("이메일 인증 실패");
            return false;
        }
    }

    private String getEmailBody(FindType findType, String email) {
        log.info("service getEmailBody() email: {}", email);

        return switch (findType) {
            case ID -> userRepository.findIdByEmail(email);
            case PWD -> generateRandomPwd(email);
        };
    }

    private String generateRandomPwd(String email) {
        Random random = new Random();
        StringBuilder pwd = new StringBuilder();
        while (pwd.length() < 9) {
            pwd.append(pool.charAt(random.nextInt(pool.length())));
        }
        updatePwd(email, pwd.toString());
        return pwd.toString();
    }

    private void updatePwd(String email, String newPwd) {
        log.info("service updatePwd() newPwd: {}", newPwd);
        String temporaryPwd = bCryptPasswordEncoder.encode(newPwd);
        try {
            userRepository.updatePwdByEmail(email, temporaryPwd);
        }catch (Exception e){
            log.info("임시 비번 저장 실패 {}",e.getMessage());
        }
    }

}
