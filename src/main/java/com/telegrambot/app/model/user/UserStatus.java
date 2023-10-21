package com.telegrambot.app.model.user;

import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_status")
@NoArgsConstructor
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserBD user;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private LocalDateTime lastUpdate;
    @ManyToOne
    @JoinColumn(name = "legal_id")
    private LegalEntity legal;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private String post;

    public UserStatus(UserBD user, UserType userType, LegalEntity legal, String post) {
        this.user = user;
        this.userType = userType;
        this.lastUpdate = LocalDateTime.now();
        this.legal = legal;
        this.post = post;
    }

    public UserStatus(UserBD user) {
        this.user = user;
        this.userType = UserType.UNAUTHORIZED;
        this.lastUpdate = LocalDateTime.now();
    }

    public UserStatus(UserBD user, LegalEntity legal, String post) {
        this.user = user;
        this.userType = getUserTypeByPost(post);
        this.lastUpdate = LocalDateTime.now();
        this.legal = legal;
        this.post = post;
    }

    public UserType getUserTypeByPost(String post) {
        String REGEX_DiRECTOR = ".*\\B(президент|директор|ректор|глава|председатель|предприниматель|управляющий)\\B.*|\\Bип\s+\\B.*|ип";
        String REGEX_ADMIN = ".*\\B(бухгалтер|администратор)\\B.*";

        if (post == null) {
            return UserType.USER;
        }
        post = post.toLowerCase();
        if (post.matches(REGEX_DiRECTOR)) {
            return UserType.DIRECTOR;
        }
        if (post.matches(REGEX_ADMIN)) {
            return UserType.ADMINISTRATOR;
        }
        return UserType.USER;
    }
}
