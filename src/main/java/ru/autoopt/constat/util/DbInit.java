package ru.autoopt.constat.util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.autoopt.constat.models.ContractRecord;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.repositories.ContractRecordRepository;
import ru.autoopt.constat.repositories.ContractorRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DbInit {

    private final ContractorRepository contractorRepository;
    private final ContractRecordRepository contractRecordRepository;

    @PostConstruct
    public void postConstruct() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        List<Contractor> indexContractors = contractorRepository.findAll();
        List<ContractRecord> indexRecords = contractRecordRepository.findAll();

        if (indexContractors.size() == 0) {
            System.out.println("Absence of Contractors data is detected. Filling table with default values");

            List<Contractor> contractors = new ArrayList<>();
            Contractor contractor1 = new Contractor("0262014762", 15);
            Contractor contractor2 = new Contractor("1655325273", 11);
            Contractor contractor3 = new Contractor("3849031759", 17);
            Contractor contractor4 = new Contractor("5015012336", 24);
            Contractor contractor5 = new Contractor("5017050954", 13);
            Contractor contractor6 = new Contractor("2540186843", 16);
            Contractor contractor7 = new Contractor("2465288642", 31);
            Contractor contractor8 = new Contractor("2222044316", 15);
            Contractor contractor9 = new Contractor("4205002373", 22);
            Contractor contractor10 = new Contractor("5013019921", 19);

            contractors.add(contractor1);
            contractors.add(contractor2);
            contractors.add(contractor3);
            contractors.add(contractor4);
            contractors.add(contractor5);
            contractors.add(contractor6);
            contractors.add(contractor7);
            contractors.add(contractor8);
            contractors.add(contractor9);
            contractors.add(contractor10);

            contractorRepository.saveAll(contractors);


            if (indexRecords.size() == 0) {
                System.out.println("Absence of ContractRecords data is detected. Filling table with default values");
                List<ContractRecord> contractRecords = new ArrayList<>();

                contractRecords.add(new ContractRecord("01.03.2023", 7, 5, contractor1));
                contractRecords.add(new ContractRecord("02.03.2023", 7, 10, contractor2));
                contractRecords.add(new ContractRecord("01.03.2023", 21, 0, contractor3));
                contractRecords.add(new ContractRecord("04.03.2023", 14, 7, contractor4));
                contractRecords.add(new ContractRecord("05.03.2023", 21, 0, contractor5));
                contractRecords.add(new ContractRecord("02.03.2023", 45, 3, contractor6));
                contractRecords.add(new ContractRecord("03.03.2023", 45, 1, contractor7));
                contractRecords.add(new ContractRecord("04.03.2023", 30, 7, contractor8));
                contractRecords.add(new ContractRecord("01.03.2023", 14, 0, contractor9));
                contractRecords.add(new ContractRecord("05.03.2023", 30, 0, contractor10));

                contractRecords.add(new ContractRecord("25.03.2023", 7, 0, contractor1));
                contractRecords.add(new ContractRecord("17.03.2023", 7, 5, contractor2));
                contractRecords.add(new ContractRecord("01.04.2023", 21, 0, contractor3));
                contractRecords.add(new ContractRecord("30.03.2023", 14, 2, contractor4));
                contractRecords.add(new ContractRecord("01.04.2023", 21, 6, contractor5));
                contractRecords.add(new ContractRecord("21.04.2023", 45, 0, contractor6));
                contractRecords.add(new ContractRecord("20.04.2023", 45, 3, contractor7));
                contractRecords.add(new ContractRecord("12.04.2023", 30, 1, contractor8));
                contractRecords.add(new ContractRecord("17.03.2023", 14, 7, contractor9));
                contractRecords.add(new ContractRecord("06.04.2023", 30, 0, contractor10));

                contractRecords.add(new ContractRecord("07.04.2023", 7, 1, contractor1));
                contractRecords.add(new ContractRecord("03.04.2023", 7, 3, contractor2));
                contractRecords.add(new ContractRecord("27.04.2023", 21, 0, contractor3));
                contractRecords.add(new ContractRecord("20.04.2023", 14, 5, contractor4));
                contractRecords.add(new ContractRecord("28.04.2023", 21, 2, contractor5));
                contractRecords.add(new ContractRecord("05.06.2023", 45, 6, contractor6));
                contractRecords.add(new ContractRecord("10.06.2023", 45, 0, contractor7));
                contractRecords.add(new ContractRecord("14.05.2023", 30, 13, contractor8));
                contractRecords.add(new ContractRecord("10.04.2023", 14, 1, contractor9));
                contractRecords.add(new ContractRecord("10.05.2023", 30, 0, contractor10));

                contractRecordRepository.saveAll(contractRecords);

            }
        }
    }

}
