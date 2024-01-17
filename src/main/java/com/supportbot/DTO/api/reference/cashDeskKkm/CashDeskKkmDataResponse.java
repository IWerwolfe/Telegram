package com.supportbot.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskKkmDataResponse extends DataEntityResponse<CashDeskKkmResponse> {
}
