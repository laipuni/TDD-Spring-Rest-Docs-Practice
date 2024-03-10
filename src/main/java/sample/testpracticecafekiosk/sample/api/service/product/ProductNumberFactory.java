package sample.testpracticecafekiosk.sample.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String getNextProductNumber(){
        String latestProductNumber = productRepository.getLatestProductNumber();

        if(latestProductNumber == null){
            return "001";
        }

        return createNextProductNumber(latestProductNumber);
    }

    private String createNextProductNumber(String productNumber){
        int nowProductNumber = Integer.parseInt(productNumber);
        int nextProductNumber = nowProductNumber + 1;

        return String.format("%03d",nextProductNumber);
    }
}
