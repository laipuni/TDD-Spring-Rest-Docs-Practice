package sample.testpracticecafekiosk.sample.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.testpracticecafekiosk.sample.api.ControllerTestSupport;
import sample.testpracticecafekiosk.sample.api.service.order.OrderService;
import sample.testpracticecafekiosk.sample.domain.order.request.OrderCreateRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class OrderControllerTest extends ControllerTestSupport {

    @DisplayName("주문 번호 목록을 받아서 주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        //given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001"))
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @DisplayName("주문 번호 목록이 비었다면 에러가 발생한다.")
    @Test
    void createOrderWith() throws Exception {
        //given
        List<String> productNumbers = new ArrayList<>();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(productNumbers)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/orders/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 상품이 1개이상부터 주문이 가능합니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

}