package com.devan.expensestracker.service;

import com.devan.expensestracker.model.SalaryRecord;
import com.devan.expensestracker.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("salary")
public class SalaryService extends ServiceBaseImpl<SalaryRecord, Long> {

    public SalaryService(SalaryRepository repository) {
        setRepository(repository);
    }

    @Override
    public void create(SalaryRecord record) {
        SalaryRecord lastRecord = getRepository().findFirstByOrderByDateDesc();
        if (Objects.nonNull(lastRecord)) {
            Long last = lastRecord.getDate().getEpochSecond();
            Long current = record.getDate().getEpochSecond();
            if (!(current - last < 86400 && lastRecord.getAmount().equals(record.getAmount()))) {
                super.create(record);
            }
        } else {
            super.create(record);
        }
    }
}
