package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public class AnalyticsEnricher implements Enricher {

    @Override
    public String getApiMethod() {
        return "analytics";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());


        JsonNode countEmployees = response.get(0).get("analytics").get("q7021");
        Boolean isCountEmployeesEnough = null;
        if (countEmployees != null)
            isCountEmployeesEnough = countEmployees.intValue() > 4;

        JsonNode stateProcurements = response.get(0).get("analytics").get("q4002");
        Boolean hasStateProcurements = null;
        if (stateProcurements != null)
            hasStateProcurements = stateProcurements.intValue() > 0;


        contractorDTO.setCountEmployeesEnough(isCountEmployeesEnough);
        contractorDTO.setHasStateProcurements(hasStateProcurements);

        return contractorDTO;
    }
}
