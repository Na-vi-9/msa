package com.sparta.user.presentation.request;

import com.sparta.user.domain.model.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequestDto {

    @NotNull(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "사용자 이름은 4~10자의 영문 소문자와 숫자만 사용 가능합니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호는 8~15자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상, 10자 이하로 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 한글 또는 영문 대소문자만 사용 가능합니다.")
    private String name;

    @NotNull(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotNull(message = "슬랙 아이디는 필수입니다.")
    private String slackId;

    @NotNull(message = "권한은 필수입니다.")
    private UserRoleEnum role;
}
