package com.telegrambot.app.DTO.api.doc.cardDoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataEntityResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDocDataResponse extends DataEntityResponse<CardDocResponse> {

}
