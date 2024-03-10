package sample.testpracticecafekiosk.sample.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import sample.testpracticecafekiosk.sample.IntegrationTestSupport;
import sample.testpracticecafekiosk.sample.api.service.order.OrderService;
import sample.testpracticecafekiosk.sample.domain.order.OrderRepository;
import sample.testpracticecafekiosk.sample.domain.order.reponse.OrderResponse;
import sample.testpracticecafekiosk.sample.domain.order.request.OrderCreateRequest;
import sample.testpracticecafekiosk.sample.domain.orderProduct.OrderProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;
import sample.testpracticecafekiosk.sample.domain.stock.Stock;
import sample.testpracticecafekiosk.sample.domain.stock.StockRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.*;

class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    OrderService orderService;
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown(){
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("상품 번호 리스트를 받아 주문을 생성하고 상품 번호와 관련된 재고의 수량을 차감한다.")
    @Test
    void createOrder(){
        //given
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(SELLING,"002",4000);
        Product product3 = createProduct(SELLING,"003",5000);

        Stock stock1 = Stock.builder()
                .productNumber("001")
                .quantity(3)
                .build();
        Stock stock2 = Stock.builder()
                .productNumber("002")
                .quantity(3)
                .build();
        Stock stock3 = Stock.builder()
                .productNumber("003")
                .quantity(3)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));
        stockRepository.saveAll(List.of(stock1,stock2,stock3));
        
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","002"))
                .build();

        LocalDate now = LocalDate.of(2024,2,29);

        //when
        OrderResponse orderResult = orderService.createOrders(request,now);
        List<Stock> stocks = stockRepository.findAll();

        //then
        assertThat(orderResult)
                .extracting("registeredDateTime","totalPrice")
                .contains(now,7000);

        assertThat(orderResult.getOrderProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001",3000),
                        tuple("002",4000)
                );

        assertThat(stocks).hasSize(3)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",2),
                        tuple("002",2),
                        tuple("003",3)
                );
    }

    @DisplayName("중복된 주문번호 리스트를 받아 주문을 생성하고 중복된 상품개수만큼 재고의 수량을 감소시킨다.")
    @Test
    void createOrderWithDuplicatedProducts(){
        //given
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(SELLING,"002",4000);
        Product product3 = createProduct(SELLING,"003",5000);

        Stock stock1 = Stock.builder()
                .productNumber("001")
                .quantity(3)
                .build();
        Stock stock2 = Stock.builder()
                .productNumber("002")
                .quantity(3)
                .build();
        Stock stock3 = Stock.builder()
                .productNumber("003")
                .quantity(3)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));
        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001","002"))
                .build();

        LocalDate now = LocalDate.of(2024,2,29);

        //when
        OrderResponse orderResult = orderService.createOrders(request,now);
        List<Stock> stocks = stockRepository.findAll();

        //then
        assertThat(orderResult)
                .extracting("registeredDateTime","totalPrice")
                .contains(now,10000);
        assertThat(orderResult.getOrderProducts())
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        tuple("001",3000),
                        tuple("001",3000),
                        tuple("002",4000)
                );

        assertThat(stocks)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",1),
                        tuple("002",2),
                        tuple("003",3)
                );
    }


    @DisplayName("주문 번호 리스트에 판매 중단된 상품이 있다면 에러를 발생시킨다.")
    @Test
    void createOrderWithHoldProduct(){
        //given
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(SELLING,"002",4000);
        Product product3 = createProduct(STOP_SELLING,"003",5000);

        productRepository.saveAll(List.of(product1,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","003"))
                .build();

        //when //then
        assertThatThrownBy(() -> orderService.createOrders(request,LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("판매 중지된 상품을 주문하셨습니다.");
    }

    @DisplayName("주문한 상품의 개수가 재고보다 많을 경우 에러가 발생한다.")
    @Test
    void createOrderWithHigherThanStockQuantity(){
        //given
        LocalDate now = LocalDate.of(2024, 2, 29);
        Product product1 = createProduct(SELLING,"001",3000);
        Product product2 = createProduct(SELLING,"002",4000);
        Product product3 = createProduct(SELLING,"003",5000);

        Stock stock1 = Stock.builder()
                .productNumber("001")
                .quantity(1)
                .build();
        Stock stock2 = Stock.builder()
                .productNumber("002")
                .quantity(1)
                .build();
        Stock stock3 = Stock.builder()
                .productNumber("003")
                .quantity(1)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));
        stockRepository.saveAll(List.of(stock1,stock2,stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001","002"))
                .build();


        //when//then
        assertThatThrownBy(() -> orderService.createOrders(request, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("주문할 상품의 재고가 부족합니다.");
    }

    private static Product createProduct(ProductSellingStatus status,String productNumber,int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingType(status)
                .name("커피")
                .price(price)
                .build();
    }
}