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
                        enrichContractorWithReq(modelMapper.map(contractor, ContractorDTO.class))
                ).toList();
    }

    public Optional<Contractor> getContractorByINN(String INN) {
        return contractorRepository.findByINN(INN);
    }

    private ContractorDTO enrichContractorWithReq(ContractorDTO contractorDTO) {
        Map<String, String> params = new HashMap<>();
        params.put("inn", contractorDTO.getINN());
        JsonNode response = konturConnector.getRequest("req", params);
        // TODO сделать методы, достающие данные из ИП и ЮЛ раздельно
        String orgName = getString(response.get(0).get("UL") == null ? response.get(0).get("IP").get("fio") : response.get(0).get("UL").get("legalName").get("full"));
        contractorDTO.setOrgName(orgName);
        return contractorDTO;
    }

    private String getString(JsonNode node) {
        return String.valueOf(node).replaceAll("^\"|\"$", "").replaceAll("\\\\", "");
    }

}