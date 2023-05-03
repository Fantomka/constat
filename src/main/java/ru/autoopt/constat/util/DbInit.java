package ru.autoopt.constat.util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractorRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class DbInit {

    private final ContractorRepository contractorRepository;

    @PostConstruct
    private void postConstruct() {
        List<Contractor> index = contractorRepository.findAll();
        if (index.size() == 0) {
            contractorRepository.save(new Contractor("1001343796", 0));
            contractorRepository.save(new Contractor("1215024412", 0));
            contractorRepository.save(new Contractor("1655325273", 0));
            contractorRepository.save(new Contractor("1901065326", 0));
            contractorRepository.save(new Contractor("2222044316", 0));
            contractorRepository.save(new Contractor("3849031759", 0));
            contractorRepository.save(new Contractor("2540186843", 0));
            contractorRepository.save(new Contractor("4632048460", 0));
            contractorRepository.save(new Contractor("5015012336", 0));
            contractorRepository.save(new Contractor("5018061444", 0));
            contractorRepository.save(new Contractor("4345093331", 0));
        }
    }

}
