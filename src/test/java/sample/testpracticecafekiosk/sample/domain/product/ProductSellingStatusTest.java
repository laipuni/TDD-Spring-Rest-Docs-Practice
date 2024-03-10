package sample.testpracticecafekiosk.sample.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ProductSellingStatusTest {

    @DisplayName("조회 가능한 상태의 ENUM값들만 리스트로 반환한다.")
    @Test
    void getDisplay(){
        //given

        //when
        List<ProductSellingStatus> result = ProductSellingStatus.getDisplay();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .extracting("text")
                .containsOnly("판매중","판매 보류");
    }

    @DisplayName("판매 중지된 상품은 true를 반환한다.")
    @Test
    void isStopSellingProduct(){
        //given
        ProductSellingStatus status = ProductSellingStatus.STOP_SELLING;

        //when
        boolean result = ProductSellingStatus.isStopSellingProduct(status);

        //then
        assertThat(result).isTrue();
    }

}