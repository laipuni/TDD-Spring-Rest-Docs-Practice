package sample.testpracticecafekiosk.sample.api.service.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;

@NoArgsConstructor
@Getter
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수 입니다.")
    private ProductType type;

    @NotNull(message = "판매 상태는 필수 입니다.")
    private ProductSellingStatus status;

    @NotBlank(message = "상품 이름은 필수 입니다.")
    private String name;

    @Positive(message = "상품 수량은 0이상 입니다.")
    private int quantity;

    @Positive(message = "상품 가격은 0이상 입니다.")
    private int price;

    @Builder
    private ProductCreateRequest(int quantity,ProductType type, ProductSellingStatus status, String name, int price) {
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String productNumber){
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingType(status)
                .name(name)
                .price(price)
                .build();
    }
}
