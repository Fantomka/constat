package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import static ru.autoopt.constat.util.common.CommonHelper.getString;

@Component
@AllArgsConstructor
public class SitesEnricher implements Enricher {

    private final KonturConnector connector;

    @Override
    public void enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getApi(contractorDTO.getINN(), "sites");

        JsonNode sites = response.get(0).get("sites");

        Boolean hasSites;
        if (sites != null) {
            hasSites = sites.size() > 0;
            if (hasSites) contractorDTO.setSite(getString(sites.get(0)));
        }
        else hasSites = false;

        contractorDTO.setHasSites(hasSites);
    }
}
