package dku.cloudcomputing.memberserver.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldBindingErrorDto {
    private String field;
    private String reason;
}