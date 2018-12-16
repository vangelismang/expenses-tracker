package com.devan.expensestracker.service;

import com.devan.expensestracker.model.ExpensesRecord;
import com.devan.expensestracker.repository.ExpensesRepository;
import org.springframework.stereotype.Service;

@Service("expenses")
public class ExpensesService extends ServiceBaseImpl<ExpensesRecord, Long> {

    public ExpensesService(ExpensesRepository repository) {
       setRepository(repository);
    }
}
