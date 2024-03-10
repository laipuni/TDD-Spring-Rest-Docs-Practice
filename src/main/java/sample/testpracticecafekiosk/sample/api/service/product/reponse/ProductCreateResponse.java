package sample.testpracticecafekiosk.sample.api.service.product.reponse;

import lombok.Builder;
import lombok.Getter;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;
import sample.testpracticecafekiosk.sample.domain.stock.Stock;

@Getter
public class ProductCreateResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;
    private int quantity;

    @Builder
    private ProductCreateResponse(int quantity, Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.quantity = quantity;
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductCreateResponse of(Product product, Stock stock){
        return ProductCreateResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(stock.getQuantity())
                .build();
    }
}
