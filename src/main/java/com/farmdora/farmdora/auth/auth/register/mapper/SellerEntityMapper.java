package com.farmdora.farmdora.auth.auth.register.mapper;

import com.farmdora.farmdoraauth.auth.register.dto.SellerRegisterDto;
import com.farmdora.farmdoraauth.entity.Address;
import com.farmdora.farmdoraauth.entity.Seller;
import com.farmdora.farmdoraauth.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SellerEntityMapper {

    public Seller toSellerEntity(User user, SellerRegisterDto dto, String saveFile , MultipartFile file) {
        return Seller.builder()
                .user(user)
                .name(dto.getName())
                .address(Address.builder()
                        .postNum(dto.getAddress().getPostNum())
                        .addr(dto.getAddress().getAddr())
                        .detailAddr(dto.getAddress().getDetailAddr())
                        .build())
                .companyNum(dto.getCompanyNum())
                .phoneNum(dto.getPhoneNum())
                .saveFile(saveFile)
                .originFile(file != null ? file.getOriginalFilename() : null)
                .isApprove(false)
                .build();
    }

}
