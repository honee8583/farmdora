package com.farmdora.farmdora.user.update.mapper;

import com.farmdora.farmdoraauth.entity.BankType;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserModifyDto;
import com.farmdora.farmdoraauth.mypage.user.update.dto.UserSelectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateMapper {

    private final BankTypeMapper bankTypeMapper;

    public UserSelectDto userToDto(User user) {
        return UserSelectDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .bankType(bankTypeMapper.bankTypeToDto(user.getBankType()))
                .sex(user.getSex())
                .accountNum(user.getAccountNum())
                .phoneNum(user.getPhoneNum())
                .address(user.getAddress())
                .build();
    }
    public void updateEntityFormDto(UserModifyDto userModifyDto, User user, BankType bankType) {
        user.updateUserInfo(
                userModifyDto.getPwd(),
                userModifyDto.getAccountNum(),
                userModifyDto.getAddress(),
                userModifyDto.getEmail(),
                userModifyDto.getPhoneNum(),
                bankType
        );
    }
}
