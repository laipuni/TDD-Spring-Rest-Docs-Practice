package sample.testpracticecafekiosk.sample.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.testpracticecafekiosk.sample.IntegrationTestSupport;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductCreateResponse;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.api.service.product.request.ProductCreateRequest;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void getSellingProducts(){
        //given
        Product americano =
                Product.builder()
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4500)
                        .build();

        Product latte =
                Product.builder()
                        .productNumber("002")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.HOLD)
                        .name("라떼")
                        .price(5000)
                        .build();

        Product blendy =
                Product.builder()
                        .productNumber("003")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.STOP_SELLING)
                        .name("블렌디")
                        .price(5500)
                        .build();

        productRepository.saveAll(List.of(americano,latte,blendy));

        //when
        List<ProductResponse> sellingProducts = productService.getSellingProducts();

        //then
        assertThat(sellingProducts).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",ProductSellingStatus.SELLING),
                        tuple("002","라떼",ProductSellingStatus.HOLD)
                );
    }

    @DisplayName("마지막에 등록된 상품의 번호에 +1한 번호로 상픔을 등록한다.")
    @Test
    void createProduct(){
        //given
        Product americano =
                Product.builder()
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4500)
                        .build();

        ProductCreateRequest request =
                ProductCreateRequest.builder()
                        .type(ProductType.BOTTLE)
                        .status(ProductSellingStatus.SELLING)
                        .name("망고주스")
                        .quantity(5)
                        .price(3800)
                        .build();

        productRepository.saveAll(List.of(americano));

        //when
        ProductCreateResponse createdProduct = productService.createProduct(request);

        //then
        assertThat(createdProduct.getProductNumber()).isEqualTo("002");
    }

    @DisplayName("등록할 상품의 번호로 재고를 저장한다.")
    @Test
    void findAllByProductNumberIn(){
        //given
        Product americano =
                Product.builder()
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4500)
                        .build();

        ProductCreateRequest request =
                ProductCreateRequest.builder()
                        .type(ProductType.BOTTLE)
                        .status(ProductSellingStatus.SELLING)
                        .name("망고주스")
                        .quantity(5)
                        .price(3800)
                        .build();

        productRepository.saveAll(List.of(americano));

        //when
        ProductCreateResponse createdProduct = productService.createProduct(request);

        //then
        assertThat(createdProduct.getQuantity()).isEqualTo(5);
    }

}