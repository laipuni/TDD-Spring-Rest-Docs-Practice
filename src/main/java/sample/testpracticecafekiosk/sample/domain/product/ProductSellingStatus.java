package sample.testpracticecafekiosk.sample.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductSellingStatus {

    SELLING("판매중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매 중지");

    private final String text;

    public static List<ProductSellingStatus> getDisplay(){
        return List.of(SELLING,HOLD);
    }

    public static boolean isStopSellingProduct(ProductSellingStatus status){
        return STOP_SELLING.equals(status);
    }
}
