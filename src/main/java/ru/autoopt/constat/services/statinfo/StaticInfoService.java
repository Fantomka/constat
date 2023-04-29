package ru.autoopt.constat.services.statinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

@Service
public class StaticInfoService {


    private final ObjectMapper objectMapper;
    private final Map<StatInfoType, Map<String, Object>> dictionaries = new HashMap<>();

    @Autowired
    @SuppressWarnings("unchecked")
    public StaticInfoService(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        for (StatInfoType type: StatInfoType.values()) {
            String resourcePath = "static" + FileSystems.getDefault().getSeparator() + type.name() + ".json";
            ClassPathResource staticDataResource = new ClassPathResource(resourcePath);
            dictionaries.put(type, this.objectMapper.readValue(staticDataResource.getInputStream(), Map.class));
        }
    }

    public <T> T getValue(StatInfoType statInfoType, String key, Class<T> tClass) {
        return tClass.cast(dictionaries.get(statInfoType).get(key));
    }
}
