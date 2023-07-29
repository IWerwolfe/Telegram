package com.telegrambot.app.model.user;    /*
 *created by WerWolfe on UserBD
 */

import com.telegrambot.app.model.documents.docdata.PersonData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserBD extends com.telegrambot.app.model.Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Boolean notValid = false;
    private Boolean isMaster = false;
    private PersonData person;
//    private String guid;
//    private List<UserStatus> statuses;

    public UserBD(String phone) {
        this.phone = phone;
    }
}
