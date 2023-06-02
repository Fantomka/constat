package ru.autoopt.constat.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.services.models.ContractorService;


@Component
public class ContractorNotExistsValidator implements Validator {

    private final ContractorService contractorService;

    @Autowired
    public ContractorNotExistsValidator(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Contractor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ContractorDTO contractor = (ContractorDTO) target;

        if (contractorService.getContractorByINN(contractor.getINN()).isEmpty() && contractor.getINN().length() == 10) {
            errors.rejectValue("INN", "", "Этого контрагента нет в системе");
        }

    }
}
