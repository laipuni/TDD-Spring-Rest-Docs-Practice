package sample.testpracticecafekiosk.sample.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.testpracticecafekiosk.sample.api.controller.order.OrderController;
import sample.testpracticecafekiosk.sample.api.controller.product.ProductController;
import sample.testpracticecafekiosk.sample.api.service.order.OrderService;
import sample.testpracticecafekiosk.sample.api.service.product.ProductService;
import sample.testpracticecafekiosk.sample.docs.order.OrderControllerDocsTest;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class
})
public class ControllerTestSupport {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected ProductService productService;
}
