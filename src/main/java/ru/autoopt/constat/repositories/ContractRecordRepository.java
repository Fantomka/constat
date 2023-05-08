package ru.autoopt.constat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.autoopt.constat.models.ContractRecord;

@Repository

public interface ContractRecordRepository extends JpaRepository<ContractRecord, Long> {

}
