package dku.cloudcomputing.memberserver.service.dto.controller;

import dku.cloudcomputing.memberserver.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @NotEmpty(message = "이메일은 비어있을 수 없습니다")
    private String email;

    @NotEmpty(message = "비밀번호는 비어있을 수 없습니다")
    private String password;

    @NotEmpty(message = "닉네임은 비어있을 수 없습니다")
    private String nickname;

    public Member convertToMember(PasswordEncoder passwordEncoder) {
        return new Member(getEmail(), passwordEncoder.encode(getPassword()), getNickname());
    }
}
