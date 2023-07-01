package com.telegrambot.app.model.user;    /*
 *created by WerWolfe on UserBD
 */

import com.telegrambot.app.model.PersonFields;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "users")
public class UserBD {

    @Id
    private Long id;
    private Boolean isBot;
    private String userName;
    private String languageCode;
    private Boolean canJoinGroups;
    private Boolean canReadAllGroupMessages;
    private Boolean supportInlineQueries;
    private Boolean isPremium;
    private Boolean addedToAttachmentMenu;
    private String phone;
    private Boolean notValid;
    private Boolean isEmployee;
    private Boolean isMaster;
    private PersonFields person;
//    private List<UserStatus> statuses;
}
