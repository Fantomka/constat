package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

@Component
@AllArgsConstructor
public class PledgerEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getApi(contractorDTO, "pledger");

        Boolean hasLeasing = response.get(0).get("pledges").isEmpty();

        contractorDTO.setIsLeasingOk(hasLeasing);

        return contractorDTO;
    }
}
