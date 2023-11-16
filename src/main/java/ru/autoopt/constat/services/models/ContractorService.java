package ru.autoopt.constat.services.models;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.ContractRecord;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractRecordRepository;
import ru.autoopt.constat.repositories.ContractorRepository;
import ru.autoopt.constat.services.calculators.Calculator;
import ru.autoopt.constat.services.calculators.StatusCode;
import ru.autoopt.constat.services.statinfo.StatInfoType;
import ru.autoopt.constat.services.statinfo.StaticInfoService;
import ru.autoopt.constat.util.kontur.enrichers.PetitionersOfArbitrationEnricher;
import ru.autoopt.constat.util.kontur.enrichers.ReqEnricher;
import ru.autoopt.constat.util.kontur.enrichers.SitesEnricher;

import java.text.NumberFormat;
import java.util.*;

import static ru.autoopt.constat.util.common.CommonHelper.getDatePlusNMonth;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ContractorService {

    private final ContractorRepository contractorRepository;
    private final ContractRecordRepository contractRecordRepository;
    private final Calculator calculator;
    private final ReqEnricher reqEnricher;
    private final PetitionersOfArbitrationEnricher petitionersOfArbitrationEnricher;
    private final StaticInfoService staticInfoService;
    private final SitesEnricher sitesEnricher;
    private final NumberFormat numberFormat;

    public Optional<Contractor> getContractorByINN(String INN) {
        return contractorRepository.findByINN(INN);
    }

    @Transactional
    public void save(ContractorDTO contractorDTO) {
        contractorRepository.save(contractorDTO.toEntity());
    }

    @Transactional
    public void addContract(ContractorDTO contractorDTO, ContractRecord contract) {
        Contractor contractor = contractorRepository.findByINN(contractorDTO.getINN()).get();
        contract.setContractor(contractor);
        contractRecordRepository.save(contract);
    }

    @Transactional
    public void deleteContractorByInn(String inn) {
        contractorRepository.deleteByINN(inn);
    }

    @Transactional
    public void deleteContractById(Long id) {
        contractRecordRepository.deleteById(id);
    }

    public List<ContractRecord> getContractsByINN(String inn) {
        Contractor contractor = contractorRepository.findByINN(inn).get();
        Hibernate.initialize(contractor.getContracts());
        return contractor.getContracts();
    }

    public record OverdueResult(int numberOfOverduePayments, int amountOfOverduePayments) {}

    public List<Contractor> index(Integer page, Integer contractorPerPage) {
        if (page != null && contractorPerPage != null){
            return contractorRepository.findAll(PageRequest.of(page, contractorPerPage)).getContent();
        } else return contractorRepository.findAll();
    }

    public StatementForm counterpartyVerification(ContractorDTO contractorDTO) {

        StatementForm result = new StatementForm();

        StatusCode statusCode = calculator.calculate(contractorDTO);
        int periodByAreaCode = staticInfoService.getValue(StatInfoType.REGION_RU, contractorDTO.getINN().substring(0, 2), Integer.class);

        int rate = contractorDTO.getRate();
        int limitPeriod = 0;
        int limitPeriodDenominator = 1;

        if (rate <= 10) {
            result.getAdditionalWarnings().add("Кредит не разрешен - только полная предоплата ! \n");
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
            result.setResultString("Кредит разрешен на " + limitPeriod + " дней, сумма - " + numberFormat.format(limitAmount) + "\n");
        }

        if (statusCode != StatusCode.SUCCESSFULLY) {
            result.getAdditionalWarnings().add(staticInfoService.getValue(StatInfoType.STATUS_CODE_MESSAGES, statusCode.name(), String.class));
        }

        return result;
    }

    public StatementForm recalculate(ContractorDTO contractorDTO) {

        StatementForm result = new StatementForm();

        Contractor contractor = contractorRepository.findByINN(contractorDTO.getINN()).get();
        Hibernate.initialize(contractor.getContracts());
        calculator.calculate(contractorDTO);

        int primaryRate = contractor.getRate();
        int secondaryRate = contractorDTO.getRate();

        result.getAdditionalInfo().add("Предыдущий балл скоринга: " + primaryRate);
        result.getAdditionalInfo().add("Текущий балл скоринга: " + secondaryRate);

        int nMonth;
        int numberOfOverduePaymentsLimit = 3;
        int amountOfOverduePaymentsLimit;

        if (contractor.getContracts().size() == 0) {
            result.getAdditionalWarnings().add("Для просмотра переоценки, необходимо добавить в историю хотя бы один контракт.");
        } else {

            int creditPaymentTerm = contractor.getContracts().get(0).getExpiresAfter();

            if (secondaryRate <= 10) {
                result.getAdditionalWarnings().add("Требуется полная предоплата по текущему скорингу.");
                return result;
            }

            if (contractor.getContracts().get(contractor.getContracts().size()-1).getDaysOverdue() >= 30) {
                result.getAdditionalWarnings().add("Требуется полная предоплата по причине просрочки более 30 дней при последнем выдаче кредита");
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
            if (countResult.numberOfOverduePayments() >= numberOfOverduePaymentsLimit) {
                result.getAdditionalWarnings().add("Отказано в увеличении суммы лимита по причине большого количества просрочек");
                return result;
            }

            if (countResult.amountOfOverduePayments() >= amountOfOverduePaymentsLimit)  {
                result.getAdditionalWarnings().add("Отказано в увеличении суммы лимита по причине длительности просрочек");
                return result;
            }

            double limitPeriodDenominator = 0;

            if (primaryRate > 10 && primaryRate <= 20) {
                if (secondaryRate <= 20) limitPeriodDenominator = 3.8;
                else if (secondaryRate <= 35) limitPeriodDenominator = 3.6;
                else if (secondaryRate <= 50) limitPeriodDenominator = 3.4;
            } else if (primaryRate > 21 && primaryRate <= 35) {
                if (secondaryRate <= 20) limitPeriodDenominator = 2.8;
                else if (secondaryRate <= 35) limitPeriodDenominator = 2.6;
                else if (secondaryRate <= 50) limitPeriodDenominator = 2.4;
            } else if (primaryRate > 36 && primaryRate <= 50) {
                if (secondaryRate <= 20) limitPeriodDenominator = 1.8;
                else if (secondaryRate <= 35) limitPeriodDenominator = 1.6;
                else if (secondaryRate <= 50) limitPeriodDenominator = 1.4;
            }


            double limitAmount = (double) contractorDTO.getRevenue() / 365 * creditPaymentTerm / limitPeriodDenominator;
            result.setResultString("Кредит разрешен на " + creditPaymentTerm + " дней, сумма - " + numberFormat.format(Math.ceil(limitAmount)) + "\n");
        }
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

    public List<ContractorDTO> indexContractorsInDangerZone() {
        List<Contractor> contractors = contractorRepository.findAll();
        List<ContractorDTO> result = new LinkedList<>();
        for (Contractor contractor: contractors) {
            Hibernate.initialize(contractor.getContracts());
            ContractorDTO contractorDTO = new ContractorDTO();
            contractorDTO.setINN(contractor.getINN());

            reqEnricher.enrich(contractorDTO);
            petitionersOfArbitrationEnricher.enrich(contractorDTO);
            sitesEnricher.enrich(contractorDTO);

            if (
                !contractorDTO.getIsStatusOk() ||
                !contractorDTO.getLastHeadChangeDateOk() ||
                !contractorDTO.getIsSumPetitionersOfArbitrationOk()
            ) result.add(0, contractorDTO);
            else if (contractor.getContracts().get(0).getExpiresAfter() >= 30) result.add(contractorDTO);
        }


        return result;
    }
}