package sample.testpracticecafekiosk.sample.domain.order.reponse;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.domain.order.Order;
import sample.testpracticecafekiosk.sample.domain.order.OrderStatus;
import sample.testpracticecafekiosk.sample.domain.product.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponse {

    private Long id;
    private OrderStatus status;
    private int totalPrice;
    private LocalDate registeredDateTime;
    private List<ProductResponse> orderProducts = new ArrayList<>();

    @Builder
    private OrderResponse(Long id, OrderStatus status, int totalPrice, LocalDate registeredDateTime, List<ProductResponse> orderProducts) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = orderProducts;
    }

    public static OrderResponse of(Order order,List<Product> products){
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .orderProducts(
                        products.stream()
                                .map(ProductResponse::of)
                                .toList()
                )
                .build();
    }

}
