package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleBaseValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errResponse = new HashMap<>();

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {} ", errors);

        errResponse.put("error", errors.toString());
        return errResponse;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    public Map<String, String> handleAppException(Throwable ex) {
        Map<String, String> errResponse = new HashMap<>();

        log.error("Ошибка выполнения: {} ", ex.getMessage());

        errResponse.put("error", ex.getMessage());
        return errResponse;
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleValidationException(RuntimeException ex) {
        Map<String, String> errResponse = new HashMap<>();

        log.error("Ошибка доступа: {} ", ex.getMessage());

        errResponse.put("error", ex.getMessage());

        HttpStatus httpStatus = switch (ex.getClass().getSimpleName()) {
            case "NotFoundException" -> HttpStatus.NOT_FOUND;
            case "ValidationException" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(errResponse, httpStatus);
    }

}
