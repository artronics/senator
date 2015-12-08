package artronics.senator.mvc.controllers;

import artronics.senator.mvc.resources.ValidationErrorListRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@ControllerAdvice
@EnableWebMvc
public class RestErrorHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ValidationErrorListRes> processValidationError(
            MethodArgumentNotValidException ex)
    {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ValidationErrorListRes validationErrorListRes = processFieldErrors(fieldErrors);
        validationErrorListRes.setData(result.getTarget());

        return new ResponseEntity<>
                (validationErrorListRes, HttpStatus.BAD_REQUEST);
    }

    private ValidationErrorListRes processFieldErrors(List<FieldError> fieldErrors)
    {
        ValidationErrorListRes dto = new ValidationErrorListRes();

        for (FieldError fieldError : fieldErrors) {
            dto.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return dto;
    }
}
