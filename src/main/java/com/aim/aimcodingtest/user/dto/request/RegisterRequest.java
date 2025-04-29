package com.aim.aimcodingtest.user.dto.request;

import com.aim.aimcodingtest.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterRequest {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    //todo : 아이디 룰 검증 봐야함
    @Pattern(regexp = "^[a-z][A-Za-z\\d._-]{5,20}",
            message = "아이디는 영문으로 시작하고, 영문/숫자/특수기호(.,_,-)로 4자이상 20자 이내로 사용 가능합니다")
    String username;
    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    //todo : 패스워드 룰 검증 봐야함
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]){8,20}",
            message = "패스워드는 영문으로 시작하고, 영문/숫자/특수기호만 사용 가능합니다")
    String password;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    String name;
    @Email
    String email;
    String phoneNumber;
    String address;

    public User toUser() {
        return new User(this.username, this.password, this.name, this.email, this.phoneNumber, this.address);
    }
}
