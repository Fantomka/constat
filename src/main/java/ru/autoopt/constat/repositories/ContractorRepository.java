package ru.autoopt.constat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.autoopt.constat.models.Contractor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    Optional<Contractor> findByINN(String INN);

    List<Contractor> findByRateLessThan(int threshold);

}
