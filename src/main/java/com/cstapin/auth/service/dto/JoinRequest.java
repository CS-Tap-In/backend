package com.cstapin.auth.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class JoinRequest {
    @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
    @NotEmpty
    private String username;

    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$",
            message = "영문, 숫자, 특수기호 조합 8~15자 이내로 작성해주세요"
    )
    private String password;

    @NotEmpty
    @Size(min = 3)
    private String nickname;

    private String secretKey;
}
