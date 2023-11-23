package com.macmil.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.macmil.consumer.datamodel.Product;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/*
    The below tests perform two actions:
    - Define a contract
    - Use that contract to execute an integration test
*/

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "provider", port = "8080")
public class ConsumerPactTest {
    /*
        A method annotated with @Pact defines a contract with provider and consumer
        stated. A DSL is used to describe the request and response. Although, provider's
        state can also be described using DLS.

        This forms the following test pattern:
            Given the provider is in a known state
            When the provider receives a request
            Then the provider's response is as expected
    */
    @Pact(provider = "provider", consumer = "consumer")
    RequestResponsePact providerPact(PactDslWithProvider builder) throws IOException {
        String body = readFile("widget.json");

        return builder
                .uponReceiving("Get request for widget")
                    .path("/product/widget")
                    .method(HttpMethod.GET.name())
                .willRespondWith()
                    .status(HttpStatus.OK.value())
                    .headers(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .body(body)
                .toPact();
    }

    /*
        An integration test using the contract defined in providerPact method.
        This allows to test the client connector code against the same payload,
        as used to verify the provider API. This payload is served by a mock server
        controlled by Pact.
    */
    @Test
    @PactTestFor(pactMethod = "providerPact")
    void testFetch(MockServer mockServer) {
        ProviderApiClient client = new ProviderApiClient(new RestTemplateBuilder(), mockServer.getUrl());
        Product actual = client.fetch("widget");

        Product expected = Product.builder()
                .catalogueId("1")
                .name("widget")
                .price(5.99)
                .manufacturer("Company A")
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    /*
        Utility methods
    */

    private String readFile(String fileName) throws IOException {
        File file = new File(Objects.requireNonNull(getClass().getResource(String.format("/api-responses/%s", fileName))).getFile());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    private static Map<String, String> header(String header, String value) {
        return Collections.singletonMap(header, value);
    }
}
