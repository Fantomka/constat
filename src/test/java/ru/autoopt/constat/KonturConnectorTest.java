package ru.autoopt.constat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import ru.autoopt.constat.config.KonturConfigProperties;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.util.kontur.KonturConnector;


public class KonturConnectorTest {

    @Test
    public void konturGetRequestTest() {

        KonturConnector konturConnector = new KonturConnector(
                new RestTemplate(),
                new ObjectMapper(),
                new KonturConfigProperties("", "")
        );

        JsonNode response = konturConnector.getApi("1644015657", "accountingReports");
        System.out.println(response);
    }

}
