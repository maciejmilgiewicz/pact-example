package com.macmil.provider.datamodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private final String catalogueId;
    private final String name;
    private final double price;
    private final String manufacturer;
    private final String inStock;
}
