package ru.autoopt.constat.services.calculators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.enrichers.*;


@Service
@AllArgsConstructor
public class RateCalculatorService implements Calculator {

    private final AccountingReportsEnricher accountingReportsEnricher;
    private final AnalyticsEnricher analyticsEnricher;
    private final LesseeEnricher lesseeEnricher;
    private final PetitionersOfArbitrationEnricher petitionersOfArbitrationEnricher;
    private final PledgerEnricher pledgerEnricher;
    private final ReqEnricher reqEnricher;
    private final SitesEnricher sitesEnricher;
    private final TrademarkEnricher trademarkEnricher;


    @Override
    public StatusCode calculate(ContractorDTO contractorDTO) {

        reqEnricher.enrich(contractorDTO);

        if (!contractorDTO.getIsStatusOk()) {
            return StatusCode.REORGANIZATION;
        } else if (!contractorDTO.getFoundingDateOk()) {
            return StatusCode.FOUND_DATE_LATE;
        }

        accountingReportsEnricher.enrich(contractorDTO);
        sitesEnricher.enrich(contractorDTO);
        trademarkEnricher.enrich(contractorDTO);
        pledgerEnricher.enrich(contractorDTO);
        petitionersOfArbitrationEnricher.enrich(contractorDTO);
        lesseeEnricher.enrich(contractorDTO);
        analyticsEnricher.enrich(contractorDTO);

        addRate(contractorDTO, checkCondition(contractorDTO.getCountEmployeesEnough()) ? 5 : -5);
        addRate(contractorDTO, checkCondition(contractorDTO.getHasSites()) ? 1 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getLastHeadChangeDateOk()) ? 1 : -5);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsRevenueOk()) ? 5 : -10);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsLeasingOk()) ? 3 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getHasStateProcurements()) ? 3 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsPledgesOk()) ? 3 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getHasTradeMarks()) ? 3 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsSumPetitionersOfArbitrationOk()) ? 0 : -10);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsMainFoundOk()) ? 1 : 0);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsStockIndustryOk()) ? 5 : -5);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsReceivablesOk()) ? 3 : -3);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsAccountsPayableOk()) ? 5 : -10);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsGrossProfitOk()) ? 3 : -3);
        addRate(contractorDTO, checkCondition(contractorDTO.getIsNetIncomeOk()) ? 3 : -3);

        if (contractorDTO.getHasMassOrgAddress()) {
            return StatusCode.MASS_ORG_ADDRESS;
        } else {
            addRate(contractorDTO, 3);
            return StatusCode.SUCCESSFULLY;
        }
    }

    private void addRate(ContractorDTO contractorDTO, int value) {
        contractorDTO.setRate(contractorDTO.getRate() + value);
    }

    private boolean checkCondition(Boolean boolValue) {
        if (boolValue != null) {
            return boolValue.booleanValue();
        } else return false;
    }

}
