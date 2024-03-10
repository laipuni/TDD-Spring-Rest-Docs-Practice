package sample.testpracticecafekiosk.sample.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @DisplayName("주문한 상품의 개수만큼 재고를 감소한다.")
    @Test
    void deductQuantity(){
        //given
        Stock stock = Stock.builder()
                .quantity(5)
                .productNumber("001")
                .build();

        //when
        stock.deductQuantity(2);
        //then
        assertThat(stock.getQuantity()).isEqualTo(3);
    }

    @DisplayName("재고 수량보다 높은 수량을 차감할 경우 에러를 발생한다.")
    @Test
    void deductQuantityWithHigherThanStockQuantity(){
        //given
        Stock stock = Stock.builder()
                .quantity(1)
                .productNumber("001")
                .build();

        //when //then
        assertThatThrownBy(() -> stock.deductQuantity(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("충분한 재고를 가지고 있지 않습니다.");
    }

    @DisplayName("차감할 수량이 재고 수량보다 높을 경우 true반환한다.")
    @Test
    void isHigherThanQuantity(){
        //given
        Stock stock = Stock.builder()
                .quantity(1)
                .productNumber("001")
                .build();

        //when //then
        assertThat(stock.isHigherThanQuantity(5)).isTrue();
    }

}