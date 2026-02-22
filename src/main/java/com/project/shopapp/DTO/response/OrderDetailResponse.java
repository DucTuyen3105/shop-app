package com.project.shopapp.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.Model.OrderDetail;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("price")
    private Float price;
    @JsonProperty("number_of_products")
    private Long numberOfProducts;
    @JsonProperty("total_money")
    private Float totalMoney;
    private String color;
    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail)
    {
        return  OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .color(orderDetail.getColor()).build();
    }
}
