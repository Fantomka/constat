package ru.autoopt.constat.services.models;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.ContractRecord;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;
import ru.autoopt.constat.services.calculators.Calculator;
import ru.autoopt.constat.services.calculators.StatusCode;
import ru.autoopt.constat.services.statinfo.StatInfoType;
import ru.autoopt.constat.services.statinfo.StaticInfoService;

import java.text.NumberFormat;
import java.util.*;

import static ru.autoopt.constat.util.common.CommonHelper.getDatePlusNMonth;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ModelMapper modelMapper;
    private final Calculator calculator;
    private final StaticInfoService staticInfoService;
    private final NumberFormat numberFormat;

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

        StatusCode statusCode = calculator.calculate(contractorDTO);
        int periodByAreaCode = staticInfoService.getValue(StatInfoType.REGION_RU, contractorDTO.getINN().substring(0, 2), Integer.class);

        int rate = contractorDTO.getRate();
        int limitPeriod = 0;
        int limitPeriodDenominator = 1;

        if (rate <= 10) {
            result.add("Кредит не разрешен - только полная предоплата ! \n");
        } else if (rate <= 20)  {
            limitPeriod = Math.max(periodByAreaCode, 7);
            limitPeriodDenominator = 4;
        }
        else if (rate <= 35)  {
            limitPeriod = Math.max(periodByAreaCode, 14);
            limitPeriodDenominator = 3;
        }
        else if (rate <= 50)  {
            limitPeriod = Math.max(periodByAreaCode, 45);
            limitPeriodDenominator = 2;
        }


        if (rate > 10) {
            long limitAmount = contractorDTO.getRevenue() / 365 * limitPeriod / limitPeriodDenominator;
            result.add("Кредит разрешен на " + limitPeriod + " дней, сумма - " + numberFormat.format(limitAmount) + "\n");
        }

        if (statusCode != StatusCode.SUCCESSFULLY) {
            result.add(staticInfoService.getValue(StatInfoType.STATUS_CODE_MESSAGES, statusCode.name(), String.class));
        }

        return result;
    }

    public List<String> recalculate(ContractorDTO contractorDTO) {
        Contractor contractor = contractorRepository.findByINN(contractorDTO.getINN()).get();
        Hibernate.initialize(contractor.getContracts());
        calculator.calculate(contractorDTO);

        List<String> result = new ArrayList<>();

        int primaryRate = contractor.getRate();
        int secondaryRate = contractorDTO.getRate();

        result.add("Предыдущий балл скоринга: " + primaryRate);
        result.add("Текущий балл скоринга: " + secondaryRate);

        int scoring = 0;
        int nMonth;
        int numberOfOverduePaymentsLimit = 3;
        int amountOfOverduePaymentsLimit;

        int creditPaymentTerm = contractor.getContracts().get(0).getExpiresAfter();

        if (secondaryRate <= 10) {
            result.add("Требуется полная предоплата по текущему скорингу.");
            return result;
        }

        if (contractor.getContracts().get(contractor.getContracts().size()-1).getDaysOverdue() >= 30) {
            result.add("Требуется полная предоплата по причине просрочки более 30 дней при последнем выдаче кредита");
            return result;
        }


        if (creditPaymentTerm <= 14) {
            nMonth = 3;
            amountOfOverduePaymentsLimit = 20;
        } else {
            nMonth = 6;
            amountOfOverduePaymentsLimit = 30;
        }

        OverdueResult countResult = countOverdues(contractor, nMonth);
        if (countResult.numberOfOverduePayments() < numberOfOverduePaymentsLimit) scoring += 2;
        else {
            result.add("Отказано в увеличении суммы лимита по причине большого количества просрочек");
            return result;
        }

        if (countResult.amountOfOverduePayments() <= amountOfOverduePaymentsLimit) scoring += 2;
        else {
            result.add("Отказано в увеличении суммы лимита по причине длительности просрочек");
            return result;
        }


        if (secondaryRate <= 20 && primaryRate > 36) {
            scoring -= 1;
        } else if (secondaryRate >= 21 && secondaryRate <= 35 && primaryRate >= 10 && primaryRate <= 20) {
            scoring += 1;
        } else if (secondaryRate >= 35 && primaryRate > 10 && primaryRate <= 35) {
            scoring += 2;
        }

        double limitPeriodDenominator = 0;

        result.add("Оценка пересмотра условий кредитования: " + scoring);

        if (scoring == 3 || scoring == 4) {
            if (secondaryRate <= 20) limitPeriodDenominator = 3.8;
            else if (secondaryRate <= 35) limitPeriodDenominator = 2.8;
            else limitPeriodDenominator = 1.8;
        } else if (scoring == 5) {
            if (secondaryRate < 35) limitPeriodDenominator = 2.6;
        } else {
            if (secondaryRate <= 50) limitPeriodDenominator = 1.4;
        }


        double limitAmount = (double) contractorDTO.getRevenue() / 365 * creditPaymentTerm / limitPeriodDenominator;
        result.add("Кредит разрешен на " + creditPaymentTerm + " дней, сумма - " + numberFormat.format(Math.ceil(limitAmount)) + "\n");
        return result;
    }

    private List<ContractRecord> filterContractsByMonths(Contractor contractor, int nMonths) {
        return contractor
                .getContracts()
                .stream()
                .filter(contractRecord ->
                        contractRecord.getGivenAt().before(new Date()) &&
                        contractRecord.getGivenAt().after(getDatePlusNMonth(nMonths))
                ).toList();
    }

    private OverdueResult countOverdues(Contractor contractor, int nMonth) {
        List<ContractRecord> contractRecords = filterContractsByMonths(contractor, -nMonth);

        int numberOfOverduePayments = 0;
        int amountOfOverduePayments = 0;

        for (ContractRecord contract : contractRecords) {
            if (contract.getDaysOverdue() != 0) {
                numberOfOverduePayments++;
                amountOfOverduePayments += contract.getDaysOverdue();
            }
        }

        return new OverdueResult(numberOfOverduePayments, amountOfOverduePayments);
    }

    @Transactional
    public void save(Contractor contractor) {
        contractorRepository.save(contractor);
    }

}