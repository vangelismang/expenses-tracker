package com.devan.expensestracker.service;

import com.devan.expensestracker.model.Record;
import com.devan.expensestracker.repository.RecordRepository;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public abstract class ServiceBaseImpl<E extends Record, ID extends Long> implements ServiceBase<E,ID> {

    private RecordRepository<E> repository;

    @Override
    public void create(E record) {
        repository.save(record);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public Instant findLastUpdateDate() {
        return repository.findLastUpdateDate();
    }

    @Override
    public List<BigDecimal> getTotalAmountPerMonth() throws ParseException {
       List<BigDecimal> results = new ArrayList<>();
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy");

        for(int i = 1; i<13; i++){
            String startString;
            String finString;
            int j = i+1;
            if (i<10) {
                startString = "02/0" + i + "/18";
                finString = "01/0" + j +"/18";
            }else if (i <12){
                startString = "02/" + i + "/18";
                finString = "01/" + j +"/18";
            }else {
                startString = "02/" + i + "/18";
                finString = "01/01/19";
            }
            Date start = df1.parse(startString);
            Date end = df1.parse(finString);
            results.add(getSumForMonth(start.toInstant(), end.toInstant()));
        }


        return results;
    }

    private BigDecimal getSumForMonth(Instant start, Instant end) {
        List<E> results =  repository.findByDateGreaterThanAndDateLessThan(start, end);
        BigDecimal sum = BigDecimal.ZERO;
        for (Record record : results) {
            sum = sum.add(record.getAmount());
        }

        return sum;
    }
}
