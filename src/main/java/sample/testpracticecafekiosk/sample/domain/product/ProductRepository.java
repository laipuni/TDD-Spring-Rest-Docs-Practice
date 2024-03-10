package sample.testpracticecafekiosk.sample.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query(value = "select p.product_Number from product p order by p.Id desc limit 1"
            ,nativeQuery = true)
    String getLatestProductNumber();
}
