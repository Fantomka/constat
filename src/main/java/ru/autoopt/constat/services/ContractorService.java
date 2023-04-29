package ru.autoopt.constat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;
import ru.autoopt.constat.services.calculators.RateCalculatorService;

import java.util.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ModelMapper modelMapper;
    private final RateCalculatorService rateCalculatorService;

    public List<ContractorDTO> index() {
        List<Contractor> index = contractorRepository.findAll();
        return index.stream()
                .map(contractor ->
                        modelMapper.map(contractor, ContractorDTO.class)
                ).toList();
    }

    public Optional<Contractor> getContractorByINN(String INN) {
        return contractorRepository.findByINN(INN);
    }

    @Transactional
    public void save(Contractor contractor) {
        contractor.setRate(0);
        contractorRepository.save(contractor);
    }

}