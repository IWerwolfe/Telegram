package com.supportbot.DTO.api.reference.legal.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractResponse extends EntityResponse {
    private String guidPartner;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date startBilling;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date date;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date stopBilling;
    private String standardHourlyRate;
    private Boolean isBilling;
    private String billingTypeString;

    @JsonCreator
    public ContractResponse(String json) {
        fillToJson(json, ContractResponse.class, this);
    }
}
