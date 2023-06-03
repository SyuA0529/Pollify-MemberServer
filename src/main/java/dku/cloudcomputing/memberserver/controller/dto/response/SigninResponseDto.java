package dku.cloudcomputing.memberserver.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponseDto {
    private String status;
    private String token;
}
