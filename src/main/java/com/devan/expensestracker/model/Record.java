package com.devan.expensestracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private Instant date;

    public Record(BigDecimal amount, Instant date) {
        this.amount = amount;
        this.date = date;
    }
}
