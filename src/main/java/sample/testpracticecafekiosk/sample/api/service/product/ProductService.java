package sample.testpracticecafekiosk.sample.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductCreateResponse;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.api.service.product.request.ProductCreateRequest;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;
import sample.testpracticecafekiosk.sample.domain.stock.Stock;
import sample.testpracticecafekiosk.sample.domain.stock.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;
    private final StockRepository stockRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request){
        //가장 최근에 등록된 상품 번호에 +1하고 저장
        String nextProductNumber = productNumberFactory.getNextProductNumber();

        //상품 등록
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        //상품 재고 등록
        Stock stock = Stock.builder()
                .productNumber(nextProductNumber)
                .quantity(request.getQuantity())
                .build();

        stockRepository.save(stock);

        return ProductCreateResponse.of(savedProduct,stock);
    }

    public List<ProductResponse> getSellingProducts(){
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.getDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
