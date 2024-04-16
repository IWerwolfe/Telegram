package com.supportbot.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
