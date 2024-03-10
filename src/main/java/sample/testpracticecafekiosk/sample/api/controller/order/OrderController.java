package sample.testpracticecafekiosk.sample.api.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sample.testpracticecafekiosk.sample.api.ApiResponse;
import sample.testpracticecafekiosk.sample.api.service.order.OrderService;
import sample.testpracticecafekiosk.sample.domain.order.reponse.OrderResponse;
import sample.testpracticecafekiosk.sample.domain.order.request.OrderCreateRequest;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request){
        OrderResponse response = orderService.createOrders(request, LocalDate.now());
        return ApiResponse.of(HttpStatus.CREATED,response);
    }


}
