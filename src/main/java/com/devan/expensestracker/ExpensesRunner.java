package com.devan.expensestracker;

import com.devan.expensestracker.service.CalculatorService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ExpensesRunner implements ApplicationRunner {

    private final CalculatorService service;

    public ExpensesRunner(CalculatorService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.calculate();
    }
}
