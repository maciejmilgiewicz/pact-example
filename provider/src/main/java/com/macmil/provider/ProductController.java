package com.macmil.provider;

import com.macmil.provider.datamodel.Product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ProductController {

    @GetMapping(path = "/product/{productName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable String productName) {

        if(productName != null && productName.equalsIgnoreCase("widget")) {
            return Product.builder()
                    .catalogueId("1")
                    .name("widget")
                    .price(5.99)
                    .manufacturer("Company A")
                    .inStock("yes")
                    .build();
        } else {
            throw new ProductNotFoundException(productName);
        }

    }
}
