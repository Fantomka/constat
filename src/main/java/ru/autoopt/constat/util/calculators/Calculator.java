package ru.autoopt.constat.util.calculators;

import ru.autoopt.constat.dto.ContractorDTO;

public interface Calculator {
    StatusCode calculate(ContractorDTO contractorDTO);
}
