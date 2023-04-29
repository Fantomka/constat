package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public class PledgerEnricher implements Enricher {
    @Override
    public String getApiMethod() {
        return "pledger";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());

        Boolean hasLeasing = response.get(0).get("pledges").isEmpty();

        contractorDTO.setIsLeasingOk(hasLeasing);

        return contractorDTO;
    }
}
