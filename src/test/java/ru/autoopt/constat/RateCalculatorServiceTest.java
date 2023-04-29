package ru.autoopt.constat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import ru.autoopt.constat.config.KonturConfigProperties;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.services.calculators.RateCalculatorService;
import ru.autoopt.constat.util.kontur.KonturConnector;
import ru.autoopt.constat.util.kontur.enrichers.*;

public class RateCalculatorServiceTest {

    @Test
    public void rateCalculatorTest() {

        KonturConnector konturConnector = new KonturConnector(
                new RestTemplate(),
                new ObjectMapper(),
                new KonturConfigProperties("", "")
        );

        RateCalculatorService rateCalculatorService = new RateCalculatorService(
                new AccountingReportsEnricher(konturConnector),
                new AnalyticsEnricher(konturConnector),
                new LesseeEnricher(konturConnector),
                new PetitionersOfArbitrationEnricher(konturConnector),
                new PledgerEnricher(konturConnector),
                new ReqEnricher(konturConnector),
                new SitesEnricher(konturConnector),
                new TrademarkEnricher(konturConnector)
        );

        ContractorDTO contractorDTO = new ContractorDTO();
        contractorDTO.setINN("1644015657");
        contractorDTO.setRate(0);
        StatusCode statusCode = rateCalculatorService.calculate(contractorDTO);
        System.out.println(contractorDTO.getRate() + " " + statusCode.toString());
    }
}
