package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

@Component
@AllArgsConstructor
public class TrademarkEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public void enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getByApi(contractorDTO.getINN(), "trademarks");

        Boolean hasTradeMarks = !response.get(0).get("trademarks").isEmpty();

        contractorDTO.setHasTradeMarks(hasTradeMarks);
    }
}
