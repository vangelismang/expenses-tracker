package com.devan.expensestracker.service;

import com.devan.expensestracker.model.Record;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;

public interface ServiceBase<E extends Record, ID extends Number> {

    void create(E record);

    List<E> findAll();

    void delete(ID id);

    Instant findLastUpdateDate();

    List<Number> getTotalAmountPerMonth() throws ParseException;
}
