package com.example.holiday.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {
    private static WireMockServer wireMockServer;

    @Value("${WIREMOCK_PORT:9561}")
    private int wiremockPort;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(WireMockConfiguration.options().port(wiremockPort));
        }
        return wireMockServer;
    }
}
