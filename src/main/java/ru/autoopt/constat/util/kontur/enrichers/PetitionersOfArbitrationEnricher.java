package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import java.util.Iterator;

import static ru.autoopt.constat.util.common.CommonHelper.dateIsBefore2022_07_01;

public class PetitionersOfArbitrationEnricher implements Enricher {
    @Override
    public String getApiMethod() {
        return "petitionersOfArbitration";
    }

    @Override
    public ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector) {
        JsonNode response = connector.getApi(contractorDTO, getApiMethod());

        Iterator<JsonNode> petitioners = response.get(0).get("petitioners").elements();

        Double sum = 0D;
        while (petitioners.hasNext()) {
            JsonNode petitioner = petitioners.next();

            if (!Boolean.TRUE.equals(dateIsBefore2022_07_01(petitioner.get("lastCaseDate")))) {
                sum += petitioner.get("y3Sum").asDouble();
            }

        }

        Boolean isSumPetitionersOfArbitrationOk = (sum / contractorDTO.getRevenue()) * 100 < 10;

        contractorDTO.setIsSumPetitionersOfArbitrationOk(isSumPetitionersOfArbitrationOk);

        return contractorDTO;
    }
}
