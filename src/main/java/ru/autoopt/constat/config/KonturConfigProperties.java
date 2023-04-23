package ru.autoopt.constat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kontur")
public record KonturConfigProperties(String apiUrl, String apiKey) { }
