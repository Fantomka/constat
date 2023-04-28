package ru.autoopt.constat.util.kontur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.autoopt.constat.config.KonturConfigProperties;
import ru.autoopt.constat.dto.ContractorDTO;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:application-dev.properties")
public class KonturConnector {


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final KonturConfigProperties konturConfigProperties;

    @Autowired
    public KonturConnector(KonturConfigProperties konturConfigProperties) {
        this.konturConfigProperties = konturConfigProperties;
    }

    public JsonNode getApi(ContractorDTO contractorDTO, String apiMethod) {
        Map<String, String> params = new HashMap<>();
        params.put("inn", contractorDTO.getINN());
        return getRequest(apiMethod, params);
    }

    private JsonNode getRequest(String apiMethod, Map<String, String> qParams) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> param : qParams.entrySet()) {
            headers.add(param.getKey(), param.getValue());
        }
        headers.add("key", konturConfigProperties.apiKey());
        URI uri = buildURI(apiMethod, headers);
        try {
            return mapper.readTree(restTemplate.getForObject(uri, String.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private URI buildURI(String apiMethod, MultiValueMap<String, String> qParams) {
        return UriComponentsBuilder
                .fromHttpUrl(konturConfigProperties.apiUrl())
                .path("/"+apiMethod)
                .queryParams(qParams)
                .build().toUri();
    }
}
