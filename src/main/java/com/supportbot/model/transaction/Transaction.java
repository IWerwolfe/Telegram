package com.supportbot.model.transaction;

import com.supportbot.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD userBD;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Lob
    private String text;
    private boolean isCommand;
    private LocalDateTime date;
    private Integer idMessage;
}
