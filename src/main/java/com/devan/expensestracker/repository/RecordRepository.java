package com.devan.expensestracker.repository;

import com.devan.expensestracker.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@NoRepositoryBean
public interface RecordRepository<E extends Record> extends JpaRepository<E, Long> {

    E findFirstByOrderByDateDesc();

    List<E> findByDateGreaterThanAndDateLessThan(Instant start, Instant end);

    default Instant findLastUpdateDate() {
        E record = findFirstByOrderByDateDesc();

        return Objects.nonNull(record) ? record.getDate() : Instant.ofEpochMilli(0);
    }
}
