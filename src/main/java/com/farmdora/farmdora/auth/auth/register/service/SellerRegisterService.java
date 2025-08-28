package com.farmdora.farmdora.auth.auth.register.service;

import com.farmdora.farmdoraauth.auth.register.dto.SellerRegisterDto;
import com.farmdora.farmdoraauth.auth.register.mapper.SellerEntityMapper;
import com.farmdora.farmdoraauth.auth.register.repository.AuthRepository;
import com.farmdora.farmdoraauth.auth.register.repository.SellerRepository;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.entity.Auth;
import com.farmdora.farmdoraauth.entity.Seller;
import com.farmdora.farmdoraauth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SellerRegisterService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final AuthRepository authRepository;
    private final SellerEntityMapper sellerEntityMapper;
    private final NCPStorageService ncpStorageService;

    public void registerSeller(int userId, SellerRegisterDto dto , MultipartFile file) {
        User user = userRepository.getReferenceById(userId);
        Auth auth = authRepository.getReferenceById(dto.getAuthId());

        String saveFile = UUID.randomUUID().toString();

        user.changeAuth(auth);
        userRepository.save(user);

        try {
            ncpStorageService.upload(saveFile,file.getInputStream(), file.getSize());

            Seller seller = sellerEntityMapper.toSellerEntity(user, dto, saveFile , file);

            sellerRepository.save(seller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
