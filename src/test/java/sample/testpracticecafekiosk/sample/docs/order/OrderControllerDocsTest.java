package sample.testpracticecafekiosk.sample.docs.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.testpracticecafekiosk.sample.api.controller.order.OrderController;
import sample.testpracticecafekiosk.sample.api.service.order.OrderService;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.docs.RestDocsSupport;
import sample.testpracticecafekiosk.sample.domain.order.Order;
import sample.testpracticecafekiosk.sample.domain.order.OrderStatus;
import sample.testpracticecafekiosk.sample.domain.order.reponse.OrderResponse;
import sample.testpracticecafekiosk.sample.domain.order.request.OrderCreateRequest;
import sample.testpracticecafekiosk.sample.domain.product.Product;
import sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class OrderControllerDocsTest extends RestDocsSupport {

    private final OrderService orderService = Mockito.mock(OrderService.class);
    @Override
    protected Object initController() {
        return new OrderController(orderService);
    }

    @DisplayName("주문 번호 목록을 받아서 주문을 생성 API")
    @Test
    void createOrder() throws Exception {
        //given
        LocalDate now = LocalDate.of(2024,3,10);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","002"))
                .build();

        List<ProductResponse> products = List.of(
                ProductResponse.builder()
                        .id(1L)
                        .type(ProductType.HANDMADE)
                        .productNumber("001")
                        .sellingStatus(ProductSellingStatus.SELLING)
                        .name("라떼")
                        .price(5000)
                        .build(),
                ProductResponse.builder()
                        .id(2L)
                        .type(ProductType.BOTTLE)
                        .productNumber("002")
                        .sellingStatus(ProductSellingStatus.SELLING)
                        .name("망고주스")
                        .price(4000)
                        .build()

        );

        String content = objectMapper.writeValueAsString(request);

        Mockito.when(orderService.createOrders(any(OrderCreateRequest.class),any(LocalDate.class)))
                        .thenReturn(
                                OrderResponse.builder()
                                        .id(1L)
                                        .registeredDateTime(now)
                                        .orderProducts(products)
                                        .totalPrice(9000)
                                        .status(OrderStatus.INIT)
                                        .build()
                        );

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/orders/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("order-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productNumbers[]").type(JsonFieldType.ARRAY)
                                        .description("주문할 상품 번호 목록")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("주문 식별키"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING)
                                        .description("주문 상태"),
                                fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("상품 총 가격"),
                                fieldWithPath("data.registeredDateTime").type(JsonFieldType.ARRAY)
                                        .description("주문 등록 날짜"),
                                fieldWithPath("data.orderProducts[]").type(JsonFieldType.ARRAY)
                                        .description("주문 상품들"),
                                fieldWithPath("data.orderProducts[].id").type(JsonFieldType.NUMBER)
                                        .description("주문 상품 식별키"),
                                fieldWithPath("data.orderProducts[].productNumber").type(JsonFieldType.STRING)
                                        .description("주문 상품 번호"),
                                fieldWithPath("data.orderProducts[].type").type(JsonFieldType.STRING)
                                        .description("주문 상품 상태"),
                                fieldWithPath("data.orderProducts[].sellingStatus").type(JsonFieldType.STRING)
                                        .description("주문 상품 판매 상태"),
                                fieldWithPath("data.orderProducts[].name").type(JsonFieldType.STRING)
                                        .description("주문 상품 이름"),
                                fieldWithPath("data.orderProducts[].price").type(JsonFieldType.NUMBER)
                                        .description("주문 상품 가격")
                        )
                        ));

    }
}
