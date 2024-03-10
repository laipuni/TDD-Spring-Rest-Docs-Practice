package sample.testpracticecafekiosk.sample.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sample.testpracticecafekiosk.sample.IntegrationTestSupport;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class ProductNumberFactoryTest extends IntegrationTestSupport {
    @Autowired
    ProductNumberFactory productNumberFactory;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown(){
        productRepository.deleteAllInBatch();
    }

    @DisplayName("가장 최근에 등록된 상품을 조회하고 조회한 상품 번호에서 1 더한다.")
    @Test
    void getNextProductNumber(){
        //given
        Product americano =
                Product.builder()
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingType(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4500)
                        .build();

        productRepository.saveAll(List.of(americano));

        //when
        String nextProductNumber = productNumberFactory.getNextProductNumber();

        //then
        assertThat(nextProductNumber).isEqualTo("002");
    }

    @DisplayName("최근에 등록된 상품이 없을 경우 상품번호는 001이다.")
    @Test
    void getNextProductNumberNotProduct(){
        //given

        //when
        String nextProductNumber = productNumberFactory.getNextProductNumber();

        //then
        assertThat(nextProductNumber).isEqualTo("001");
    }


}