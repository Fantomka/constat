package ru.autoopt.constat;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ru.autoopt.constat.config.KonturConfigProperties;
import ru.autoopt.constat.util.KonturConnector;

import java.util.HashMap;
import java.util.Map;



public class KonturConnectorTest {

    @Test
    public void konturGetRequestTest() {
        KonturConfigProperties konturConfigProperties = new KonturConfigProperties("", "");
        KonturConnector konturConnector = new KonturConnector(konturConfigProperties);
        Map<String, String> params = new HashMap<>();
        params.put("inn", "7728168971");
        JsonNode response = konturConnector.getRequest("req", params);
        System.out.println(response);
    }

}
