package com.supportbot.DTO.api.reference.cashDesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.DataEntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskDataResponse extends DataEntityResponse<CashDeskResponse> {
}
