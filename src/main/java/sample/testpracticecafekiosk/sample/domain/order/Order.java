package sample.testpracticecafekiosk.sample.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.testpracticecafekiosk.sample.BaseEntity;
import sample.testpracticecafekiosk.sample.domain.orderProduct.OrderProduct;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static sample.testpracticecafekiosk.sample.domain.order.OrderStatus.INIT;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.isStopSellingProduct;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int totalPrice;

    private LocalDate registeredDateTime;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(OrderStatus status,LocalDate registeredDateTime, List<Product> products) {
        this.status = status;
        this.registeredDateTime = registeredDateTime;
        this.totalPrice = calculateTotalPrice(products);
        this.orderProducts = products.stream()
                .filter(this::canSelling)
                .map(p -> new OrderProduct(this, p))
                .toList();
    }

    private boolean canSelling(Product product){
        if(isStopSellingProduct(product.getSellingStatus())){
            throw new IllegalArgumentException("판매 중지된 상품이 주문에 포함되어 있습니다.");
        }

        return true;
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    public static Order create(List<Product> products, LocalDate registeredDateTime){
        return Order.builder()
                .status(INIT)
                .registeredDateTime(registeredDateTime)
                .products(products)
                .build();
    }
}
