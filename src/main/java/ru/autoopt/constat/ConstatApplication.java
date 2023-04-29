package ru.autoopt.constat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.autoopt.constat.config.KonturConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(KonturConfigProperties.class)
public class ConstatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstatApplication.class, args);
    }

}
