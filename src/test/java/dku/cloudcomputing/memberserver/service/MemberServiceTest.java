package dku.cloudcomputing.memberserver.service;

import dku.cloudcomputing.memberserver.service.dto.controller.SignInRequestDto;
import dku.cloudcomputing.memberserver.service.dto.controller.SignUpRequestDto;
import dku.cloudcomputing.memberserver.entity.Member;
import dku.cloudcomputing.memberserver.exception.NoSuchMemberException;
import dku.cloudcomputing.memberserver.exception.NotMatchPasswordException;
import dku.cloudcomputing.memberserver.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:31234", "port=31234" }
)
@AutoConfigureTestDatabase
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName(value = "회원가입 성공 테스트")
    void signUpSuccess() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");

        //when
        memberService.signUp(signUpRequestDto);

        //then
        Member findMember = memberRepository.findByEmail("test@test").orElse(null);
        assertThat(findMember.getEmail()).isEqualTo(signUpRequestDto.getEmail());
        assertThat(findMember.getNickname()).isEqualTo(signUpRequestDto.getNickname());
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 이메일 null")
    void signUpFailWhenEmailIsEmpty() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(null, "test", "tester");

        //when
        //then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 이메일 중복")
    void signUpFailWhenEmailIsDuplicate() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        //when
        //then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 비밀번호 null")
    void signUpFailWhenPasswordIsEmpty() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", null, "tester");

        //when
        //then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 닉네임 null")
    void signUpFailWhenNicknameIsEmpty() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", null);

        //when
        //then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName(value = "로그인 성공 테스트")
    void signInSuccess() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        SignInRequestDto signInRequestDto = new SignInRequestDto("test@test", "test");

        //when
        String token = memberService.signIn(signInRequestDto);

        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName(value = "이메일이 존재하지 않을 때 로그인 실패 테스트")
    void signUpFailWhenMemberNotExist() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        SignInRequestDto signInRequestDto = new SignInRequestDto("notExist@test", "test");

        //when
        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @Test
    @DisplayName(value = "패스워드 불일치 로그인 실패 테스트")
    void signUpFailPasswordNotMatch() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        SignInRequestDto signInRequestDto = new SignInRequestDto("test@test", "notExist");

        //when
        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(NotMatchPasswordException.class);
    }

    @Test
    @DisplayName(value = "이메일이 null 로그인 실패 테스트")
    void signUpFailWhenEmailIsNull() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        SignInRequestDto signInRequestDto = new SignInRequestDto(null, "test");

        //when
        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(NoSuchMemberException.class);
    }

    @Test
    @DisplayName(value = "비밀번호가 null일 때 로그인 실패 테스트")
    void signUpFailWhenPasswordIsNull() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test", "test", "tester");
        memberService.signUp(signUpRequestDto);

        SignInRequestDto signInRequestDto = new SignInRequestDto("test@test", null);

        //when
        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}