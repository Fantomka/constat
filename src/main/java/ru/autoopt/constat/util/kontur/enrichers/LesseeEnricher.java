package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public class LesseeEnricher implements Enricher {
    @Override
    public String getApiMethod() {
        return "lessee";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());

        Boolean hasLeasing = response.get(0).get("contracts").isEmpty();

        contractorDTO.setIsLeasingOk(hasLeasing);

        return contractorDTO;
    }
}
