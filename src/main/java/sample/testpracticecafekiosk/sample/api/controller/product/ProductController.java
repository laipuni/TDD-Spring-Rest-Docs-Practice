package sample.testpracticecafekiosk.sample.api.controller.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.testpracticecafekiosk.sample.api.ApiCollectionResponse;
import sample.testpracticecafekiosk.sample.api.ApiResponse;
import sample.testpracticecafekiosk.sample.api.service.product.ProductService;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductCreateResponse;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.api.service.product.request.ProductCreateRequest;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products/new")
    public ApiResponse<ProductCreateResponse> createProduct(@Valid @RequestBody ProductCreateRequest request){
        ProductCreateResponse response = productService.createProduct(request);
        return ApiResponse.of(HttpStatus.CREATED,response);
    }

    @GetMapping("/products")
    public ApiCollectionResponse<ProductResponse> getSellingProducts(){
        return ApiCollectionResponse.of(HttpStatus.OK,productService.getSellingProducts());
    }

}
