package ru.autoopt.constat.util.kontur.enrichers;

import ru.autoopt.constat.dto.ContractorDTO;

public interface Enricher {

    void enrich(ContractorDTO contractorDTO);

}
