package com.telegrambot.app.model.command;

import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "commands_cache")
public class CommandCache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String command;
    private boolean isComplete;
    private String subCommand;
    @Column(columnDefinition = "text")
    private String result;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD userBD;
    private Long countStep;

    public void increment() {
        this.countStep++;
    }

    public void decrement() {
        this.countStep--;
    }
}
