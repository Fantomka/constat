package ru.autoopt.constat.services.models;

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

        int limitPeriod = 0;

        if (contractorDTO.getRate() <= 10) {
            result.add("Кредит не разрешен - только полная предоплата ! \n");
        } else if (contractorDTO.getRate() <= 20) limitPeriod = Math.max(periodByAreaCode, 7);
        else if (contractorDTO.getRate() <= 35) limitPeriod = Math.max(periodByAreaCode, 14);
        else if (contractorDTO.getRate() <= 50) limitPeriod = Math.max(periodByAreaCode, 45);


        if (contractorDTO.getRate() > 10) {
            long limitAmount = (contractorDTO.getRevenue()/365) * (limitPeriod/4);
            result.add("Кредит разрешен на " + limitPeriod + " дней, сумма - " + format.format(limitAmount) + "\n");
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