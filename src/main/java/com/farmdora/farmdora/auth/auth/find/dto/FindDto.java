package com.farmdora.farmdora.auth.auth.find.dto;

import com.farmdora.farmdoraauth.auth.find.findenum.FindType;
import lombok.Data;

@Data

public class FindDto {
    private String email;
    private String code;
    private FindType find;
}
