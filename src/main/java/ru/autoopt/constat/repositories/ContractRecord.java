package ru.autoopt.constat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ContractRecord extends JpaRepository<ContractRecord, Long> {

}
