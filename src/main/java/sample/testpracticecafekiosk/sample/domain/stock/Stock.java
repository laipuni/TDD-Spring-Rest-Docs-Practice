package sample.testpracticecafekiosk.sample.domain.stock;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.testpracticecafekiosk.sample.domain.order.Order;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public void deductQuantity(int quantity){
        if(this.quantity <quantity){
            throw new IllegalArgumentException("충분한 재고를 가지고 있지 않습니다.");
        }

        this.quantity -=quantity;
    }

    public boolean isHigherThanQuantity(int quantity){
        return this.quantity < quantity;
    }
}
