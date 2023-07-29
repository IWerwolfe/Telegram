package com.telegrambot.app.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class EnumBD1C extends Entity {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    private String name;
    private String display;

    @Override
    public String toString() {
        return getDisplay();
    }
}
