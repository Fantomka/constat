package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import java.util.Iterator;

@Component
@AllArgsConstructor
public class AccountingReportsEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public void enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getApi(contractorDTO, "accountingReports");


        // Вытаскиваем все нужные поля
        ArrayNode buhForms = ((ArrayNode) response.get(0).get("buhForms"));
        JsonNode accountingReport = buhForms.get(buhForms.size() - 1);

        ArrayNode form1 = (ArrayNode) accountingReport.get("form1");
        ArrayNode form2 = (ArrayNode) accountingReport.get("form1");

        Iterator<JsonNode> iter1 = form1.elements();
        Iterator<JsonNode> iter2 = form2.elements();

        Boolean mainFounds = false;

        Double stockIndustryStart = null;
        Double stockIndustryEnd = null;

        Double receivablesStart = null;
        Double receivablesEnd = null;

        Double accountsPayableStart = null;
        Double accountsPayableEnd = null;

        while (iter1.hasNext()) {
            JsonNode elem = iter1.next();

            if (elem.get("code").intValue() == 1150)
                mainFounds = true;

            if (elem.get("code").intValue() == 1210) {
                stockIndustryStart = elem.get("startValue").asDouble();
                stockIndustryEnd = elem.get("endValue").asDouble();
            }

            if (elem.get("code").intValue() == 1230) {
                receivablesStart = elem.get("startValue").asDouble();
                receivablesEnd = elem.get("endValue").asDouble();
            }

            if (elem.get("code").intValue() == 1520) {
                accountsPayableStart = elem.get("startValue").asDouble();
                accountsPayableEnd = elem.get("endValue").asDouble();
            }

        }

        Double revenue = null;
        Double grossProfit = null;
        Double netIncome = null;

        while (iter2.hasNext()) {
            JsonNode elem = iter2.next();

            if (elem.get("code").intValue() == 2110)
                revenue = elem.get("endValue").asDouble();

            if (elem.get("code").intValue() == 2100)
                grossProfit = elem.get("endValue").asDouble();

            if (elem.get("code").intValue() == 2400)
                netIncome = elem.get("endValue").asDouble();
        }

        // Просчитываем все поля с формулами

        Boolean isRevenueOk = revenue > 120000;
        Boolean isStockIndustryOk = (revenue / ((stockIndustryStart + stockIndustryEnd) / 2)) > 3;
        Boolean isReceivablesOk = (revenue / ((receivablesStart + receivablesEnd) / 2)) > 12;
        Boolean isAccountsPayableOk = (revenue / ((accountsPayableStart + accountsPayableEnd) / 2)) > 2.5;
        Boolean isGrossProfitOk  = (grossProfit / revenue) * 100 > 5;
        Boolean isNetIncomeOk = netIncome > 0;

        // Укладываем результаты в модель
        contractorDTO.setIsRevenueOk(isRevenueOk);
        contractorDTO.setRevenue(revenue);
        contractorDTO.setIsMainFoundOk(mainFounds);
        contractorDTO.setIsStockIndustryOk(isStockIndustryOk);
        contractorDTO.setIsReceivablesOk(isReceivablesOk);
        contractorDTO.setIsAccountsPayableOk(isAccountsPayableOk);
        contractorDTO.setIsGrossProfitOk(isGrossProfitOk);
        contractorDTO.setIsNetIncomeOk(isNetIncomeOk);

    }
}
