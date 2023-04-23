package ru.autoopt.constat.util;

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

    public ContractorDTO enrichContractorWithReq(ContractorDTO contractorDTO) {
        Map<String, String> params = new HashMap<>();
        params.put("inn", contractorDTO.getINN());
        JsonNode response = getRequest("req", params);
        //Слава богу пришла отмазка не ебаться с ИП
//        String orgName = getString(response.get(0).get("UL") == null ? response.get(0).get("IP").get("fio") : response.get(0).get("UL").get("legalName").get("full"));
        // TODO Придумать нормальный механизм вытаскивания данных (можно хоть под ковёр спрятать, лишь бы глаза не мозолило)
        String orgName = getString(response.get(0).get("UL").get("legalName").get("full"));

        contractorDTO.setOrgName(orgName);
        return contractorDTO;
    }

    private String getString(JsonNode node) {
        return String.valueOf(node).replaceAll("^\"|\"$", "").replaceAll("\\\\", "");
    }

    public JsonNode getRequest(String apiMethod, Map<String, String> qParams) {
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

//    private <T> T postRequest(String apiMethod, HttpHeaders headers, Class<T> tClass) {
//        return restTemplate.postF
//    }

    private URI buildURI(String apiMethod, MultiValueMap<String, String> qParams) {
        return UriComponentsBuilder
                .fromHttpUrl(konturConfigProperties.apiUrl())
                .path("/"+apiMethod)
                .queryParams(qParams)
                .build().toUri();
    }
}
