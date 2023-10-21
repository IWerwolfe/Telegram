package com.telegrambot.app.model.user;    /*
 *created by WerWolfe on UserResponse
 */

import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserStatus> statuses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserBalance userBalance;

    public UserBD(User user) {
        BeanUtils.copyProperties(user, this);
        this.person = new PersonData(user.getFirstName(), user.getLastName());
        this.statuses = List.of(new UserStatus(this));
        this.userBalance = new UserBalance(this);
    }

    public UserType getUserType() {
        return statuses.isEmpty() ? UserType.UNAUTHORIZED : statuses.get(0).getUserType();
    }

    public int getBalance() {
        return userBalance.getAmount();
    }

    public void setBalance(int balance) {
        userBalance.setAmount(balance);
    }

    public void updateBalance(int sum) {
        userBalance.setAmount(userBalance.getAmount() + sum);
    }

    public String getGuidEntity() {
        return this.getSyncData() == null ? null :
                this.getSyncData().getGuid();
    }

    public void setSyncData(String guid) {
        if (syncData == null) {
            this.syncData = new SyncData(guid);
        }
    }

    @Override
    public String toString() {
        return person.getFirstName() + " " + person.getLastName();
    }
}
