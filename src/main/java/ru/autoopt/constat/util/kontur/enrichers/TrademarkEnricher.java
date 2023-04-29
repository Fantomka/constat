package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public class TrademarkEnricher implements Enricher {
    @Override
    public String getApiMethod() {
        return "trademarks";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());

        Boolean hasTradeMarks = !response.get(0).get("trademarks").isEmpty();

        contractorDTO.setHasTradeMarks(hasTradeMarks);

        return contractorDTO;
    }
}
