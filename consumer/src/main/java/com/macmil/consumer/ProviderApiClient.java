package com.macmil.consumer;

import com.macmil.consumer.datamodel.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProviderApiClient {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public ProviderApiClient(RestTemplateBuilder builder, @Value("${provider.url}") String url) {

        this.restTemplate = builder.build();
        this.url = url;
    }

    public Product fetch(String productName) {

        return restTemplate.getForObject(String.format("%s/product/%s", url, productName), Product.class);
    }
}
