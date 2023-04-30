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
import ru.autoopt.constat.services.calculators.Calculator;
import ru.autoopt.constat.services.calculators.RateCalculatorService;
import ru.autoopt.constat.services.calculators.StatusCode;
import ru.autoopt.constat.services.statinfo.StatInfoType;
import ru.autoopt.constat.services.statinfo.StaticInfoService;

import java.util.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ModelMapper modelMapper;
    private final Calculator calculator;
    private final StaticInfoService staticInfoService;

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

    public void counterpartyVerification(ContractorDTO contractorDTO) {

        int limitPeriod = 0;
        double limitAmount = 0;
        String returnedResponse = "";

        StatusCode statusCode = calculator.calculate(contractorDTO);
        int periodByAreaCode = staticInfoService.getValue(StatInfoType.REGION_RU, contractorDTO.getINN().substring(0, 2), Integer.class);

        if(contractorDTO.getRate() >= 11 && contractorDTO.getRate() <= 20) {
            limitPeriod = (periodByAreaCode > 7) ? periodByAreaCode : 7;
            limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
        } else if (contractorDTO.getRate() >= 21 && contractorDTO.getRate() <= 35) {
            limitPeriod = (periodByAreaCode > 14) ? periodByAreaCode : 14;
            limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
        } else if (contractorDTO.getRate() >= 36 && contractorDTO.getRate() <= 50) {
            limitPeriod = (periodByAreaCode > 45) ? periodByAreaCode : 45;
            limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
        } else if (contractorDTO.getRate() <= 10) {
            returnedResponse += "Кредит не разрешен - только полная предоплата ! \n";
            if (statusCode == StatusCode.REORGANIZATION) {
                returnedResponse += "Компания находиться в стадии реорганизации \n";
            } else if (statusCode == StatusCode.FOUND_DATE_LATE) {
                returnedResponse += "Компания образована после 01.07.2022 \n";
            } else if (statusCode == StatusCode.MASS_ORG_ADDRESS) {
                returnedResponse += "Компания имеет несколько ЮР адресов\n";
            }
        }

        returnedResponse += "Кредит разрешен на " + limitPeriod + " дней, сумма - " + limitAmount + "\n";

    }

    @Transactional
    public void save(Contractor contractor) {
        contractor.setRate(0);
        contractorRepository.save(contractor);
    }

}