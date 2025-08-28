package com.farmdora.farmdora.user.update.service;

import com.farmdora.farmdoraauth.auth.oauth.repository.SnsRepository;
import com.farmdora.farmdoraauth.auth.register.repository.BankTypeRepository;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.entity.BankType;
import com.farmdora.farmdoraauth.entity.Sns;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserModifyDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserSelectDto;
import com.farmdora.farmdoraauth.mypage.user.update.mapper.UserUpdateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;
    private final BankTypeRepository bankTypeRepository;
    private final UserUpdateMapper userUpdateMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SnsRepository snsRepository;

    public boolean verifyPassword(int userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다", userId));
        return bCryptPasswordEncoder.matches(rawPassword, user.getPwd());
    }

    public UserSelectDto getUserById(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("유저 수정", userId));
        return userUpdateMapper.userToDto(user);
    }

    public void updateUser(int userId, UserModifyDto userModifyDto) {

        User existUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("유저 수정", userId));

        if(userModifyDto.getPwd() == null || userModifyDto.getPwd().isEmpty()) {
            userModifyDto.setPwd(existUser.getPwd());
        }else {
            userModifyDto.setPwd(bCryptPasswordEncoder.encode(userModifyDto.getPwd()));
        }

        if (userModifyDto.getEmail() == null){
            userModifyDto.setEmail(existUser.getEmail());
        }

        BankType bankType = bankTypeRepository.findById(userModifyDto.getBankId())
                .orElseThrow(()-> new ResourceNotFoundException("유저 수정 중 banktype 조회",userModifyDto.getBankId()));

        userUpdateMapper.updateEntityFormDto(userModifyDto, existUser, bankType);

        userRepository.save(existUser);
    }

    public void expireUser(int userId) {
        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("회원 탈퇴", userId));
        existUser.expireUser();
        List<Sns> snsList = snsRepository.findByUser(existUser);
        for (Sns sns : snsList) {
            sns.expireSnsName();
        }
    }
}
