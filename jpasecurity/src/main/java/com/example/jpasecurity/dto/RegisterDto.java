package com.example.jpasecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "아이디를 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다")
    private String password;
}
