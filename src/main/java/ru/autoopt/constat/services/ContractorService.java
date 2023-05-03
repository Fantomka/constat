package ru.autoopt.constat.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;
import ru.autoopt.constat.services.calculators.Calculator;
import ru.autoopt.constat.services.calculators.StatusCode;
import ru.autoopt.constat.services.statinfo.StatInfoType;
import ru.autoopt.constat.services.statinfo.StaticInfoService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
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

    public List<String> counterpartyVerification(ContractorDTO contractorDTO) {


        List<String> result = new ArrayList<>();
        NumberFormat format = NumberFormat.getInstance(new Locale("ru", "RU"));

        StatusCode statusCode = calculator.calculate(contractorDTO);
        int periodByAreaCode = staticInfoService.getValue(StatInfoType.REGION_RU, contractorDTO.getINN().substring(0, 2), Integer.class);

        if(contractorDTO.getRate() >= 11 && contractorDTO.getRate() <= 20) {
            int limitPeriod = Math.max(periodByAreaCode, 7);
            long limitAmount = (contractorDTO.getRevenue() / 365) * (limitPeriod/4);
            result.add("Кредит разрешен на " + limitPeriod + " дней, сумма - " + format.format(limitAmount) + "\n");
        } else if (contractorDTO.getRate() >= 21 && contractorDTO.getRate() <= 35) {
            int limitPeriod = Math.max(periodByAreaCode, 14);
            long limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
            result.add("Кредит разрешен на " + limitPeriod + " дней, сумма - " + format.format(limitAmount) + "\n");
        } else if (contractorDTO.getRate() >= 36 && contractorDTO.getRate() <= 50) {
            int limitPeriod = Math.max(periodByAreaCode, 45);
            long limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
            result.add("Кредит разрешен на " + limitPeriod + " дней, сумма - " + format.format(limitAmount) + "\n");
        } else if (contractorDTO.getRate() <= 10) {
            result.add("Кредит не разрешен - только полная предоплата ! \n");
        }

        if (statusCode != StatusCode.SUCCESSFULLY) {
            result.add(staticInfoService.getValue(StatInfoType.STATUS_CODE_MESSAGES, statusCode.name(), String.class));
        }

        return result;
    }

    @Transactional
    public void save(Contractor contractor) {
        contractor.setRate(0);
        contractorRepository.save(contractor);
    }

}