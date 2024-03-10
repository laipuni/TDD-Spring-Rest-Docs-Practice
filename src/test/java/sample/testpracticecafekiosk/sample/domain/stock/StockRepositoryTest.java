package sample.testpracticecafekiosk.sample.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.testpracticecafekiosk.sample.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class StockRepositoryTest extends IntegrationTestSupport {

    @Autowired
    StockRepository stockRepository;

    @DisplayName("상품 번호리스트로 재고들을 조회한다.")
    @Test
    void findAllByProductNumberIn(){
        //given
        Stock stock1 = Stock.builder()
                .productNumber("001")
                .quantity(5)
                .build();

        Stock stock2 = Stock.builder()
                .productNumber("002")
                .quantity(3)
                .build();

        stockRepository.saveAll(List.of(stock1,stock2));

        //when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        assertThat(stocks).hasSize(2)
                .extracting("productNumber","quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",5),
                        tuple("002",3)
                );
    }

}