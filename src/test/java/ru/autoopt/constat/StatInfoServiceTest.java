package ru.autoopt.constat;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.autoopt.constat.services.statinfo.StaticInfoService;

import java.io.IOException;


public class StatInfoServiceTest {

    @Test
    public void konturGetRequestTest() throws IOException {

        StaticInfoService staticInfoService = new StaticInfoService(new ObjectMapper());

        staticInfoService.getInfo();
    }
}
