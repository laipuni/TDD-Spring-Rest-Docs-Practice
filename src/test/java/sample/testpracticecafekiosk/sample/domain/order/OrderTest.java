package sample.testpracticecafekiosk.sample.domain.order;

import org.assertj.core.api.Assertions;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.SELLING;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.STOP_SELLING;

@Transactional
class OrderTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("주문을 생성할 때 주문한 상품들의 총가격을 계산한다.")
    @Test
    void calculateTotalPrice(){
        //given
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(SELLING,"002",4000);
        Product product3 = createProduct(SELLING,"003",5000);

        int totalPrice = product1.getPrice() + product2.getPrice() + product3.getPrice();

        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        Order order = Order.create(List.of(product1,product2,product3), LocalDate.now());

        //then
        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
    }

    @DisplayName("주문 생성시 등록 시간을 기록한다.")
    @Test
    void registeredDateTime(){
        //given
        LocalDate now = LocalDate.now();
        Product product1 = createProduct(SELLING,"001",3000);

        productRepository.saveAll(List.of(product1));
        //when
        Order order = Order.create(List.of(product1),now);

        //then
        assertThat(order.getRegisteredDateTime()).isEqualTo(now);
    }

    @DisplayName("판매 중지된 상품이 주문에 포함되면 에러가 발생한다.")
    @Test
    void canSelling(){
        //given
        LocalDate now = LocalDate.now();
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(STOP_SELLING,"002",4000);

        productRepository.saveAll(List.of(product1,product2));
        //when//then
        assertThatThrownBy(() -> Order.create(List.of(product1,product2),now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("판매 중지된 상품이 주문에 포함되어 있습니다.");

    }

    private static Product createProduct(ProductSellingStatus status, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingType(status)
                .name("커피")
                .price(price)
                .build();
    }


}