package com.telegrambot.app.model.command;

import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String command;
    private String result;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD userBD;
    private CommandStatus status;
    private LocalDateTime dateComplete;
}
