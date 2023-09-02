package com.telegrambot.app.model.user;

import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_status")
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserBD userBD;
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
}
