package sample.testpracticecafekiosk.sample.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.testpracticecafekiosk.sample.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("조회 가능한 상품들을 리스트로 조회한다.")
    @Test
    void findAllByProductSellingStatusIn(){
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
        List<Product> result = productRepository.findAllBySellingStatusIn(ProductSellingStatus.getDisplay());

        //then
        assertThat(result).hasSize(2)
                .extracting("productNumber","name","sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",ProductSellingStatus.SELLING),
                        tuple("002","라떼",ProductSellingStatus.HOLD)
                );
    }

    @DisplayName("상품 번호리스트로 상품들을 조회한다.")
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
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "003"));


        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노"),
                        tuple("003","블렌디")
                );
    }

    @DisplayName("가장 마지막에 등록된 상품의 번호를 조회한다")
    @Test
    void getLastestProductNumber(){
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
        String latestProductNumber = productRepository.getLatestProductNumber();

        //then
        assertThat(latestProductNumber).isEqualTo("001");
    }


}