package ru.autoopt.constat.util.kontur.enrichers;

import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;

public interface Enricher {

    String getApiMethod();

    ContractorDTO enrich(ContractorDTO contractorDTO, KonturConnector connector);

}
