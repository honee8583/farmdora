package com.farmdora.farmdora.auth.dto;

import com.farmdora.farmdora.entity.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String addr;
    private String addrDetail;
    private String zipCode;
    private LocalDate birth;
    private Gender gender;
    private String phone;
}
