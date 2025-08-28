package com.farmdora.farmdora.auth.auth.find.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {

    private String id;
    private String role;
}
