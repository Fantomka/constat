package ru.autoopt.constat.util.kontur.enrichers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

import java.util.Iterator;

import static ru.autoopt.constat.util.common.CommonHelper.dateIsBefore2022_07_01;

@Component
@AllArgsConstructor
public class PetitionersOfArbitrationEnricher implements Enricher {

    private final KonturConnector connector;


    @Override
    public void enrich(ContractorDTO contractorDTO) {
        JsonNode response = connector.getApi(contractorDTO, "petitionersOfArbitration");

        if (response.get(0).get("petitioners") == null) {
            contractorDTO.setIsSumPetitionersOfArbitrationOk(true);
            return;
        }

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
    }
}
