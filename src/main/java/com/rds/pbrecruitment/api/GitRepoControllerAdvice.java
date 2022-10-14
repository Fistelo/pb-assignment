package com.rds.pbrecruitment.api;

import com.rds.pbrecruitment.errors.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.rds.pbrecruitment.errors.ApiError.INVALID_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GitRepoControllerAdvice {
    public record ApiErrorBody(String code, String message) {}

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorBody handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        if (errorList.isEmpty()) {
            return new ApiErrorBody(INVALID_REQUEST.getCode(), INVALID_REQUEST.getErrorMessage());
        }
        return new ApiErrorBody(INVALID_REQUEST.getCode(), errorList.get(0).getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ApiErrorBody handleValidationExceptions(final ApiException    apiException, final HttpServletResponse response) {
        log.warn("Handling exception by API advisor: " + apiException.getMessage());
        response.setStatus(apiException.getStatus());
        return new ApiErrorBody(apiException.getCode(), apiException.getMessage());
    }
}
