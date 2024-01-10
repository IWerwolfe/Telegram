package com.supportbot.model.command;

import com.supportbot.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String command;
    @Column(columnDefinition = "text")
    private String result;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD userBD;
    private CommandStatus status;
    private LocalDateTime dateComplete;
}
