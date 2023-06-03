package dku.cloudcomputing.memberserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dku.cloudcomputing.memberserver.exception.NoSuchMemberException;
import dku.cloudcomputing.memberserver.service.dto.controller.SignInRequestDto;
import dku.cloudcomputing.memberserver.service.dto.controller.SignUpRequestDto;
import dku.cloudcomputing.memberserver.entity.Member;
import dku.cloudcomputing.memberserver.exception.NotMatchPasswordException;
import dku.cloudcomputing.memberserver.repository.MemberRepository;
import dku.cloudcomputing.memberserver.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    @Value(value = "${message.topic.saveMember}")
    private String saveMemberTopic;
    private final MemberRepository memberRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final ObjectMapper objectMapper;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        Member savedMember = memberRepository.save(signUpRequestDto.convertToMember(passwordEncoder));
        memberRepository.flush();
        try {
            sendMemberToKafka(savedMember);
        } catch (Exception e) {
            memberRepository.delete(savedMember);
            throw new RuntimeException(e);
        }
    }

    public String signIn(SignInRequestDto singInRequestDto) {
        Member findMember = memberRepository.findByEmail(singInRequestDto.getEmail()).
                orElseThrow(NoSuchMemberException::new);
        if (!passwordEncoder.matches(singInRequestDto.getPassword(), findMember.getPassword()))
            throw new NotMatchPasswordException();
        return jwtGenerator.createToken(findMember.getEmail());
    }

    public boolean validateEmail(String email) {
        return !memberRepository.existsByEmail(email);
    }

    private void sendMemberToKafka(Member savedMember) throws JsonProcessingException {
        String memberJson = objectMapper.writeValueAsString(savedMember);
        kafkaTemplate.send(saveMemberTopic, memberJson);
    }
}
