package sample.testpracticecafekiosk.sample.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.testpracticecafekiosk.sample.api.ApiResponse;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> bindException(BindException e){
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors()
                        .get(0).getDefaultMessage(),
                null);
    }

}
