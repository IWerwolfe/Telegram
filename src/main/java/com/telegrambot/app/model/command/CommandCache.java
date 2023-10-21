package com.telegrambot.app.model.command;

import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "commands_cache")
@NoArgsConstructor
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

    public CommandCache(String command, String subCommand, UserBD userBD, String result) {
        this(command, subCommand, userBD);
        this.result = result;
    }

    public CommandCache(String command, String subCommand, UserBD userBD) {
        this.command = command;
        this.subCommand = subCommand;
        this.userBD = userBD;
        this.countStep = 0L;
    }

    public CommandCache(String command, UserBD userBD) {
        this.command = command;
        this.userBD = userBD;
        this.countStep = 0L;
    }

    public void increment() {
        this.countStep++;
    }

    public void decrement() {
        this.countStep--;
    }
}
