package com.aim.aimcodingtest.user.dto.request;

import com.aim.aimcodingtest.user.entity.User;
import com.aim.aimcodingtest.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterRequest {
    @NotBlank(message = "아이디는 필수 입력 값입니다")
    @Pattern(regexp = "^[a-z][A-Za-z\\d._-]{3,19}$",
            message = "아이디는 영문으로 시작하고, 영문/숫자/특수기호(.,_,-)로 4자이상 20자 이내로 사용 가능합니다")
    String username;
    @NotBlank(message = "패스워드는 필수 입력 값입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,20}$",
            message = "패스워드는 영문/숫자/특수기호를 각각 1가지 이상 사용하고, 8자 이상 20자 이내로 사용 가능합니다")
    String password;
    @NotBlank(message = "이름은 필수 입력 값입니다")
    String name;
    @Email
    String email;
    String phoneNumber;
    String address;
}
