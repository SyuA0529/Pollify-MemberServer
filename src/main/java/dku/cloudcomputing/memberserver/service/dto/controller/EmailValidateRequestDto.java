package dku.cloudcomputing.memberserver.service.dto.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EmailValidateRequestDto {
    @NotEmpty
    private String email;
}
