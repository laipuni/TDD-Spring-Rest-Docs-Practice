package sample.testpracticecafekiosk.sample.docs.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.testpracticecafekiosk.sample.api.controller.product.ProductController;
import sample.testpracticecafekiosk.sample.api.service.product.ProductService;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductCreateResponse;
import sample.testpracticecafekiosk.sample.api.service.product.reponse.ProductResponse;
import sample.testpracticecafekiosk.sample.api.service.product.request.ProductCreateRequest;
import sample.testpracticecafekiosk.sample.docs.RestDocsSupport;
import sample.testpracticecafekiosk.sample.domain.product.ProductType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.testpracticecafekiosk.sample.domain.product.ProductSellingStatus.SELLING;
import static sample.testpracticecafekiosk.sample.domain.product.ProductType.BOTTLE;
import static sample.testpracticecafekiosk.sample.domain.product.ProductType.HANDMADE;

public class ProductControllerDocsTest extends RestDocsSupport {

    private final ProductService productService = mock(ProductService.class);
    @Override
    protected Object initController() {
        return new ProductController(productService);
    }

    @DisplayName("신규 상품을 등록하는 API")
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

        Mockito.when(productService.createProduct(any(ProductCreateRequest.class)))
                .thenReturn(ProductCreateResponse.builder()
                                .id(1L)
                                .productNumber("001")
                                .type(HANDMADE)
                                .name("애플망고주스")
                                .price(5000)
                                .quantity(5)
                                .sellingStatus(SELLING)
                                .build()
                        );

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("type").type(STRING)
                                        .description("product type"),
                                fieldWithPath("status").type(STRING)
                                        .description("상품 판매 상태"),
                                fieldWithPath("name").type(STRING)
                                        .description("상품 이름"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                        .description("상품 개수"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("상태 코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("응답 메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("응답 데이터"),
                                fieldWithPath("data.id").type(NUMBER)
                                                .description("DB에 저장된 상품 식별키"),
                                fieldWithPath("data.productNumber").type(STRING)
                                                .description("상품 번호"),
                                fieldWithPath("data.type").type(STRING)
                                        .description("상품 타입"),
                                fieldWithPath("data.sellingStatus").type(STRING)
                                        .description("상품 판매 상태"),
                                fieldWithPath("data.name").type(STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.quantity").type(NUMBER)
                                        .description("상품 개수"),
                                fieldWithPath("data.price").type(NUMBER)
                                        .description("상품 가격")
                                )));

    }

    @DisplayName("판매중인 상품들 조회 API")
    @Test
    void getSellingProducts() throws Exception {
        //given
        List<ProductResponse> responses = List.of(
                ProductResponse.builder()
                        .id(1L)
                        .productNumber("001")
                        .sellingStatus(SELLING)
                        .type(HANDMADE)
                        .price(5000)
                        .name("커피")
                        .build(),
                ProductResponse.builder()
                        .id(2L)
                        .productNumber("002")
                        .sellingStatus(SELLING)
                        .type(BOTTLE)
                        .price(4000)
                        .name("망고주스")
                        .build()
        );

        when(productService.getSellingProducts()).thenReturn(responses);

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("product-selling",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(STRING)
                                        .description("응답 상태"),
                                fieldWithPath("message").type(STRING)
                                        .description("응답 메세지"),
                                fieldWithPath("size").type(NUMBER)
                                        .description("전체 상품 개수"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(NUMBER)
                                        .description("상품 식별키"),
                                fieldWithPath("data[].productNumber").type(STRING)
                                        .description("상품 번호"),
                                fieldWithPath("data[].type").type(STRING)
                                        .description("상품 타입"),
                                fieldWithPath("data[].sellingStatus").type(STRING)
                                        .description("상품 판매 타입"),
                                fieldWithPath("data[].name").type(STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data[].price").type(NUMBER)
                                        .description("상품 가격")
                        )));
    }
}
