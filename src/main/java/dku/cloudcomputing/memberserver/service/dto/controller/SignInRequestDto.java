package dku.cloudcomputing.memberserver.service.dto.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequestDto {
    @NotEmpty(message = "이메일은 비어있을 수 없습니다")
    private String email;

    @NotEmpty(message = "비밀번호는 비어있을 수 없습니다")
    private String password;
}
