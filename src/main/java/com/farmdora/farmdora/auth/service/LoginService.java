package com.farmdora.farmdora.auth.service;

import com.farmdora.farmdora.auth.dto.JoinRequestDto;
import com.farmdora.farmdora.auth.dto.LoginRequestDto;
import com.farmdora.farmdora.auth.dto.LoginUser;
import com.farmdora.farmdora.auth.dto.TokenResponseDto;

public interface LoginService {

    void join(JoinRequestDto joinRequestDto);

    TokenResponseDto login(LoginRequestDto loginRequestDto);

    LoginUser getLoginUser(Long id);

    TokenResponseDto reissueTokens(String refreshToken);

}
