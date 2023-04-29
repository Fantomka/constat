package ru.autoopt.constat.services.calculators;

import ru.autoopt.constat.dto.ContractorDTO;

public interface Calculator {
    StatusCode calculate(ContractorDTO contractorDTO);
}
