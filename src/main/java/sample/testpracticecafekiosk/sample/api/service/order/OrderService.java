package sample.testpracticecafekiosk.sample.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.testpracticecafekiosk.sample.domain.order.Order;
import sample.testpracticecafekiosk.sample.domain.order.OrderRepository;
import sample.testpracticecafekiosk.sample.domain.order.reponse.OrderResponse;
import sample.testpracticecafekiosk.sample.domain.order.request.OrderCreateRequest;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductRepository;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.stock.Stock;
import sample.testpracticecafekiosk.sample.domain.stock.StockRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sample.testpracticecafekiosk.sample.domain.order.OrderStatus.INIT;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.isStopSellingProduct;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Transactional
    public OrderResponse createOrders(OrderCreateRequest request, LocalDate registeredDateTime){
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        //판매 중지된 상품이 있는지 검증
        validateStopSellingProduct(products);

        //중복된 상품 각각을 리스트에 원소로 저장
        List<Product> duplicatedProducts = findProductBy(productNumbers,products);

        //주문한 상품의 개수만큼 상품 재고의 수량을 차감
        deductStockQuantity(productNumbers);

        Order order = Order.create(duplicatedProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder,duplicatedProducts);
    }

    private void deductStockQuantity(List<String> productNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Stock> stockMap = createStockMapBy(stocks);
        Map<String, Long> productCounting = createProductCountingBy(productNumbers);

        stockMap.forEach((key,value)->{
            int quantity = productCounting.get(key).intValue();

            if(value.isHigherThanQuantity(quantity)){
                throw new IllegalArgumentException("주문할 상품의 재고가 부족합니다.");
            }

            value.deductQuantity(quantity);
        });
    }

    private List<Product> findProductBy(List<String> productNumbers,List<Product> products) {
        Map<String, Product> productsMap = products.stream()
                        .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                        .map(productsMap::get)
                        .collect(Collectors.toList());
    }

    private void validateStopSellingProduct(List<Product> products) {
        for (Product p : products) {
            if (isStopSellingProduct(p.getSellingStatus())) {
                throw new IllegalArgumentException("판매 중지된 상품을 주문하셨습니다.");
            }
        }
    }

    private static Map<String, Long> createProductCountingBy(List<String> productNumbers) {
        return productNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private static Map<String, Stock> createStockMapBy(List<Stock> stocks) {
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }

}
