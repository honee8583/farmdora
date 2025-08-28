package com.farmdora.farmdora.user.update.controller;

import com.farmdora.farmdoraauth.auth.register.dto.SellerRegisterDto;
import com.farmdora.farmdoraauth.auth.register.message.StandardRegisterMassage;
import com.farmdora.farmdoraauth.auth.register.service.SellerRegisterService;
import com.farmdora.farmdoraauth.common.exception.BaseException;
import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserModifyDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserSelectDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.VerifyPasswordDto;
import com.farmdora.farmdoraauth.mypage.user.update.message.UserUpdateMassage;
import com.farmdora.farmdoraauth.mypage.user.update.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Slf4j
@Transactional
@RestController
@RequestMapping("${api.prefix}/mypage/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserUpdateService userUpdateService;
    private final SellerRegisterService sellerRegisterService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPassword(Principal principal, @RequestBody VerifyPasswordDto password) {
        Integer userId = Integer.parseInt(principal.getName());

        if (userUpdateService.verifyPassword(userId, password.getPwd())) {
            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, UserUpdateMassage.PASSWORD_VERIFY_SUCCESS.getMessage(), true));
        } else {
            throw new BaseException(UserUpdateMassage.PASSWORD_VERIFY_FAILURE.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detail(Principal principal) {
        Integer userId = Integer.parseInt(principal.getName());

        UserSelectDto user = userUpdateService.getUserById(userId);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, UserUpdateMassage.USER_SELECT_SUCCESS.getMessage(), user));
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyUser(Principal principal, @RequestBody UserModifyDto userModifyDto) {

        Integer userId = Integer.parseInt(principal.getName());
        try {
            userUpdateService.updateUser(userId, userModifyDto);

            return ResponseEntity.ok()
                    .body(new HttpResponse(HttpStatus.OK, UserUpdateMassage.USER_MODIFY_SUCCESS.getMessage(), true));

        } catch (Exception e) {
            throw new BaseException(UserUpdateMassage.USER_MODIFY_FAILURE.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping("/expire")
    public ResponseEntity<?> expireUser(Principal principal, @RequestBody VerifyPasswordDto password) {

        Integer userId = Integer.parseInt(principal.getName());
        if (userUpdateService.verifyPassword(userId, password.getPwd())) {
            userUpdateService.expireUser(userId);
        } else {
            throw new BaseException(UserUpdateMassage.PASSWORD_VERIFY_FAILURE.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, UserUpdateMassage.USER_EXPIRE_SUCCESS.getMessage(), true));
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> registerSeller(Principal principal, @RequestPart("seller") SellerRegisterDto sellerRegisterDto,
                                            @RequestPart("file") MultipartFile file) {

        Integer userId = Integer.parseInt(principal.getName());

        sellerRegisterService.registerSeller(userId, sellerRegisterDto, file);

        return ResponseEntity.ok()
                .body(new HttpResponse(HttpStatus.OK, StandardRegisterMassage.SELLER_REGISTER_SUCCESS.getMessage(), true));
    }
}
