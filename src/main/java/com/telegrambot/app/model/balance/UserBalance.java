package com.telegrambot.app.model.balance;

import com.telegrambot.app.model.types.Balance;
import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_balances")
@NoArgsConstructor
public class UserBalance extends Balance {
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserBD user;

    public UserBalance(UserBD user) {
        this.user = user;
        setAmount(0);
    }
}
