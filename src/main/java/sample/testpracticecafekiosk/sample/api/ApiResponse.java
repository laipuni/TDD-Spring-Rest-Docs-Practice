package sample.testpracticecafekiosk.sample.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data){
        return new ApiResponse<T>(status,message,data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data){
        return new ApiResponse<T>(status,status.name(),data);
    }

}
