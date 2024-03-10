package sample.testpracticecafekiosk.sample.domain.product;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.testpracticecafekiosk.sample.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String productNumber;

    @Enumerated(value = EnumType.STRING)
    private ProductType type;

    @Enumerated(value = EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private Product(String productNumber, ProductType type, ProductSellingStatus sellingType, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingType;
        this.name = name;
        this.price = price;
    }

}
