package com.telegrambot.app.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users_activity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD user;
    private LocalDateTime lastActivityDate;

    public UserActivity(UserBD user) {
        this.user = user;
        this.lastActivityDate = LocalDateTime.now();
    }
}
