package dku.cloudcomputing.memberserver.repository;

import dku.cloudcomputing.memberserver.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원 조회")
    void findMemberByEmailTest() {
        //given
        Member member = new Member("test@test.com", "test", "tester");
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByEmail("test@test.com");
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.orElseGet(null)).isEqualTo(member);
    }

    @Test
    @DisplayName("회원가입시 이메일은 null이 될 수 없음")
    void emailCanNotBeNullTest() {
        //givne
        Member member = new Member(null, "test", "tester");

        //when
        memberRepository.save(member);

        //then
        assertThatThrownBy(() -> memberRepository.flush())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원가입시 비밀번호는 null이 될 수 없음")
    void passwordCanNotBeNullTest() {
        //givne
        Member member = new Member("test@test.com", null, "tester");

        //when
        memberRepository.save(member);

        //then
        assertThatThrownBy(() -> memberRepository.flush())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원가입시 닉네임은 null이 될 수 없음")
    void nicknameCanNotBeNullTest() {
        //givne
        Member member = new Member("test@test.com", "test", null);

        //when
        memberRepository.save(member);

        //then
        assertThatThrownBy(() -> memberRepository.flush())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원가입시 이메일이 중복인 회원이 존재하면 에러 발생")
    void duplicatedEmailBeError() {
        Member member1 = new Member("test@test", "test", "tester1");
        Member member2 = new Member("test@test", "test", "tester2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        assertThatThrownBy(() -> memberRepository.flush())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("특정 이메일을 사용하는 회원 존재 여부 확인 - 성공")
    void checkSpecificEmailMemberExistFail() {
        Member member = new Member("test@test", "test", "tester1");
        memberRepository.save(member);

        boolean result = memberRepository.existsByEmail("test@test");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("특정 이메일을 사용하는 회원 존재 여부 확인 - 성공")
    void checkSpecificEmailMemberExistSuccess() {
        Member member = new Member("test@test", "test", "tester1");
        memberRepository.save(member);

        boolean result = memberRepository.existsByEmail("notExist@test");
        assertThat(result).isFalse();
    }
}