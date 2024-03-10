package sample.testpracticecafekiosk.sample.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.testpracticecafekiosk.sample.api.ControllerTestSupport;
import sample.testpracticecafekiosk.sample.api.service.product.ProductService;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.api.service.product.request.ProductCreateRequest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.SELLING;
import static sample.testpracticecafekiosk.sample.domain.product.ProductType.HANDMADE;


class ProductControllerTest extends ControllerTestSupport {

    @DisplayName("상품 등록 정보를 받아와서 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .price(5000)
                .name("애플망고주스")
                .status(SELLING)
                .quantity(5)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isOk());

    }

    @DisplayName("상품 등록 정보에 ProductType이 Null값으로 넘어올 경우 에러가 발생한다.")
    @Test
    void createProductWithNullProductType() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(null)
                .price(5000)
                .name("애플망고주스")
                .status(SELLING)
                .quantity(5)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("상품 등록 정보에 ProductSellingStatus이 Null값으로 넘어올 경우 에러가 발생한다.")
    @Test
    void createProductWithNullProductSellingStatus() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .price(5000)
                .name("애플망고주스")
                .status(null)
                .quantity(5)
                .build();

        String content = objectMapper.writeValueAsString(request);
        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("판매 상태는 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("상품 등록 정보에 name이 Null, 비었거나 빈칸으로 넘어올 경우 에러가 발생한다.")
    @Test
    void createProductWithNullOrEmptyOrBlankName() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .price(5000)
                .name(null)
                .status(SELLING)
                .quantity(5)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("상품 등록 정보에 quantity가 0보다 작을 경우 에러가 발생한다.")
    @Test
    void createProductWithZeroEqualThanLowerQuantity() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .price(5000)
                .name("애플망고주스")
                .status(SELLING)
                .quantity(-1)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 수량은 0이상 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("상품 등록 정보에 price가 0보다 작을 경우 에러가 발생한다.")
    @Test
    void createProductWithZeroEqualThanLowerPrice() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .price(-3000)
                .name("애플망고주스")
                .status(SELLING)
                .quantity(5)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 0이상 입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매중인 상품들을 리스트로 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        //given
        List<ProductResponse> responses = List.of();

        when(productService.getSellingProducts()).thenReturn(responses);

        //when//then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
        )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

    }
}