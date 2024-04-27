package com.supportbot.bot.subCommand;

import com.supportbot.DTO.message.MessageText;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
@SuperBuilder
public class DescriptionHandler extends TextHandler {

    @Override
    public String getStartMessage() {
        return MessageText.getStartDescription();
    }

    @Override
    public String getErrorMessage() {
        return MessageText.getErrorDescription();
    }

    @Override
    public String getDescription() {
        return "";
    }


}
