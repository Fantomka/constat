package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

@Component
@AllArgsConstructor
public class SitesEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getApi(contractorDTO, "sites");

        JsonNode sites = response.get(0).get("sites");

        Boolean hasSites;
        if (sites != null) hasSites = sites.size() > 0;
        else hasSites = false;

        contractorDTO.setHasSites(hasSites);
        return contractorDTO;
    }
}
