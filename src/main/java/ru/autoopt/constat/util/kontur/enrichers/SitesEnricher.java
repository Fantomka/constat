package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public class SitesEnricher implements Enricher {
    @Override
    public String getApiMethod() {
        return "sites";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());

        JsonNode sites = response.get(0).get("sites");

        Boolean hasSites;
        if (sites != null) hasSites = sites.size() > 0;
        else hasSites = false;

        contractorDTO.setHasSites(hasSites);
        return contractorDTO;
    }
}
