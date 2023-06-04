package com.telegrambot.app.model;    /*
 *created by WerWolfe on Bot
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "bots")
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BotType botType;
    private String token;
    private String paymentToken;
}
