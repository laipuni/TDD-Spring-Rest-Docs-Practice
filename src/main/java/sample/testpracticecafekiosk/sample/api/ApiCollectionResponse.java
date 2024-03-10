package sample.testpracticecafekiosk.sample.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor
@Getter
public class ApiCollectionResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private int size;
    private List<T> data;

    private ApiCollectionResponse(HttpStatus status, String message,int size, List<T> data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.size = size;
        this.data = data;
    }

    public static <T> ApiCollectionResponse<T> of(HttpStatus status, List<T> data){
        return new ApiCollectionResponse<T>(status,status.name(), data.size(), data);
    }
}
