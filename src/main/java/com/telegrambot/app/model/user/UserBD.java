package com.telegrambot.app.model.user;    /*
 *created by WerWolfe on UserResponse
 */

import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserBD {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private SyncData syncData;

    public String getGuidEntity() {
        return this.getSyncData() == null ? null :
                this.getSyncData().getGuid();
    }

    public void setSyncData(String guid) {
        if (syncData == null) {
            this.syncData = new SyncData(guid);
        }
    }

//    private String guid;
//    private List<UserStatus> statuses;

    public UserBD(String phone) {
        this.phone = phone;
    }
}
