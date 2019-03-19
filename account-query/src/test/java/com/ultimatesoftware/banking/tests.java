package com.ultimatesoftware.banking;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = {ConfigurationConstants.INTERNAL_MOCKS, ConfigurationConstants.HTTP_MOCKS})
public class tests {
    @Inject
    @Client("/api/v1/accounts")
    RxHttpClient client;

    @Test
    public void lol() {
        // Arrange

        // Act
        String response = client.toBlocking().retrieve(HttpRequest.GET(""));
    }
}