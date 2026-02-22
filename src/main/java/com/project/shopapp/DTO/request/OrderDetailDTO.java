package com.project.shopapp.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "Order's ID must be greater than 0")
    private Long orderId;
    @Min(value = 1, message = "Product's ID must be greater than 0")
    @JsonProperty("product_id")
    private Long productId;
    @Min(value = 0, message = "Product's price must be greater than 0")
    private Float price;
    @JsonProperty("number_of_products")
    @Min(value = 1, message = "number_of_products must be >= 1")
    private Long numberOfProducts;
    @JsonProperty("total_money")
    @Min(value = 0, message = "total money must be greater than 0")
    private Float totalMoney;
    private String color;
}
