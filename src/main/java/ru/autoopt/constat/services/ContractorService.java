package ru.autoopt.constat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ContractorService {

    private final ContractorRepository contractorRepository;

    @Autowired
    public ContractorService(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    public List<Contractor> index() {
        return contractorRepository.findAll();
    }

    public Optional<Contractor> getContractorByINN(String INN) {
        return contractorRepository.findByINN(INN);
    }

}