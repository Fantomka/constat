package ru.autoopt.constat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.autoopt.constat.config.KonturConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(KonturConfigProperties.class)
public class ConstatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstatApplication.class, args);
    }

}
