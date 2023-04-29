package ru.autoopt.constat;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.autoopt.constat.services.statinfo.StatInfoType;
import ru.autoopt.constat.services.statinfo.StaticInfoService;


import java.io.IOException;


public class StatInfoServiceTest {

    @Test
    public void statInfoGetValueTest() throws IOException {

        StaticInfoService staticInfoService = new StaticInfoService(new ObjectMapper());

        Assertions.assertEquals(21, staticInfoService.getValue(StatInfoType.REGION_RU, "04", Integer.class));
    }
}
