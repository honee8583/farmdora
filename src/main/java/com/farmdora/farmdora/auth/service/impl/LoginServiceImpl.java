package com.farmdora.farmdora.auth.service.impl;

import com.farmdora.farmdora.auth.dto.JoinRequestDto;
import com.farmdora.farmdora.auth.dto.LoginRequestDto;
import com.farmdora.farmdora.auth.dto.LoginUser;
import com.farmdora.farmdora.auth.dto.TokenResponseDto;
import com.farmdora.farmdora.auth.exception.InvalidPasswordException;
import com.farmdora.farmdora.auth.service.LoginService;
import com.farmdora.farmdora.auth.util.JwtUtil;
import com.farmdora.farmdora.common.error.exception.EntityNotFoundException;
import com.farmdora.farmdora.entity.Address;
import com.farmdora.farmdora.entity.Role;
import com.farmdora.farmdora.entity.User;
import com.farmdora.farmdora.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void join(JoinRequestDto joinRequestDto) {

        log.info("### 회원가입 정보: {}", joinRequestDto.toString());

        User user = User.builder()
                .username(joinRequestDto.getUsername())
                .password(passwordEncoder.encode(joinRequestDto.getPassword()))
                .name(joinRequestDto.getName())
                .email(joinRequestDto.getEmail())
                .address(
                        Address.builder()
                                .addr(joinRequestDto.getAddr())
                                .addrDetail(joinRequestDto.getAddrDetail())
                                .zipCode(joinRequestDto.getZipCode())
                                .build()
                )
                .birth(joinRequestDto.getBirth())
                .gender(joinRequestDto.getGender())
                .phone(joinRequestDto.getPhone())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User", String.valueOf(loginRequestDto.getUsername())));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        Long id = user.getId();
        Role role = Role.USER;
        return TokenResponseDto.builder()
                .accessToken(jwtUtil.createAccessToken(id, role))
                .refreshToken(jwtUtil.createRefreshToken(id, role))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginUser getLoginUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", String.valueOf(id)));

        return LoginUser.builder()
                .id(id)
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public TokenResponseDto reissueTokens(String refreshToken) {
        LoginUser loginUser = jwtUtil.verify(refreshToken);

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", String.valueOf(loginUser.getId())));

        return TokenResponseDto.builder()
                .accessToken(jwtUtil.createAccessToken(user.getId(), user.getRole()))
                .refreshToken(jwtUtil.createRefreshToken(user.getId(), user.getRole()))
                .build();
    }

}