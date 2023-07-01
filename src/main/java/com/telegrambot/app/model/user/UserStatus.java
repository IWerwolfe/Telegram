package com.telegrambot.app.model.user;

import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "users_status")
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD userBD;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private LocalDateTime lastUpdate;
    @ManyToOne
    @JoinColumn(name = "Legal_id")
    private LegalEntity legal;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private String post;
}
