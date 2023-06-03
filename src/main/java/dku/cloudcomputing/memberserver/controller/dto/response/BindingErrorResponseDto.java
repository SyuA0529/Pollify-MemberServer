package dku.cloudcomputing.memberserver.controller.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BindingErrorResponseDto {
    private String status;
    private List<FieldBindingErrorDto> errors = new ArrayList<>();

    public BindingErrorResponseDto(String status) {
        this.status = status;
    }
}