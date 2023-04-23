package ru.autoopt.constat.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;
import ru.autoopt.constat.util.KonturConnector;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final KonturConnector konturConnector;
    private final ModelMapper modelMapper;

    @Autowired
    public ContractorService(ContractorRepository contractorRepository, KonturConnector konturConnector, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.contractorRepository = contractorRepository;
        this.konturConnector = konturConnector;
        this.modelMapper = modelMapper;
    }

    public List<ContractorDTO> index() {
        List<Contractor> index = contractorRepository.findAll();
        return index.stream()
                .map(contractor ->
                        konturConnector.enrichContractorWithReq(modelMapper.map(contractor, ContractorDTO.class))
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