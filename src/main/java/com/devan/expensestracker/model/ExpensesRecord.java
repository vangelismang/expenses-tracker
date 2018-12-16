package com.devan.expensestracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExpensesRecord extends Record {

    public ExpensesRecord(BigDecimal amount, Instant date) {
        super(amount, date);
    }
}
