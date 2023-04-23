package ru.autoopt.constat.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.services.ContractorService;


@Component
public class ContractorValidator implements Validator {

    private final ContractorService contractorService;

    @Autowired
    public ContractorValidator(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Contractor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Contractor contractor = (Contractor) target;

        if (contractorService.getContractorByINN(contractor.getINN()).isPresent()) {
            errors.rejectValue("INN", "", "Этот контрагент уже существует в системе");
        }

    }
}
