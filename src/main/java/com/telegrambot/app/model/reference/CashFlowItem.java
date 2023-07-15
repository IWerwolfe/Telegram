package com.telegrambot.app.model.reference;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Cash_flow_items")
@NoArgsConstructor
public class CashFlowItem extends Reference {
}
