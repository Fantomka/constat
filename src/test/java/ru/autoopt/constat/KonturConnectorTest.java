package ru.autoopt.constat;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ru.autoopt.constat.config.KonturConfigProperties;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;


public class KonturConnectorTest {

    @Test
    public void konturGetRequestTest() {
        KonturConfigProperties konturConfigProperties = new KonturConfigProperties("", "");
        KonturConnector konturConnector = new KonturConnector(konturConfigProperties);

        ContractorDTO contractorDTO = new ContractorDTO("1644015657", 0);
        JsonNode response = konturConnector.getApi(contractorDTO, "accountingReports");
        System.out.println(response);
    }

}
