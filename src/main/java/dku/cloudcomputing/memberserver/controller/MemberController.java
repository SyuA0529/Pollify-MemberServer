package dku.cloudcomputing.memberserver.controller;

import dku.cloudcomputing.memberserver.exception.FieldBindException;
import dku.cloudcomputing.memberserver.service.dto.controller.EmailValidateRequestDto;
import dku.cloudcomputing.memberserver.service.dto.controller.SignInRequestDto;
import dku.cloudcomputing.memberserver.service.dto.controller.SignUpRequestDto;
import dku.cloudcomputing.memberserver.controller.dto.response.SigninResponseDto;
import dku.cloudcomputing.memberserver.controller.dto.response.StatusResponseDto;
import dku.cloudcomputing.memberserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/signup")
    public StatusResponseDto signUp(@Validated @RequestBody SignUpRequestDto signUpRequestDto,
                                    BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new FieldBindException(bindingResult.getFieldErrors());
        memberService.signUp(signUpRequestDto);
        return new StatusResponseDto("success");
    }

    @PostMapping("/auth/signin")
    public SigninResponseDto signIn(@Validated @RequestBody SignInRequestDto signInRequestDto,
                                    BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new FieldBindException(bindingResult.getFieldErrors());
        String token = memberService.signIn(signInRequestDto);
        return new SigninResponseDto("success", token);
    }

    @PostMapping("/auth/email")
    public StatusResponseDto validateEmail(@Validated @RequestBody EmailValidateRequestDto emailValidateDto,
                                           BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new FieldBindException(bindingResult.getFieldErrors());
        boolean validate = memberService.validateEmail(emailValidateDto.getEmail());
        return new StatusResponseDto(validate? "success" : "fail");
    }
}
