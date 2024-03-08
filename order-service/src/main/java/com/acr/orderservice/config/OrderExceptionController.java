
package com.acr.orderservice.config;

import com.acr.basedomains.dto.Order;
import com.acr.orderservice.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionController {

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<Object> exception(OrderNotFoundException exception) {

        return new ResponseEntity<Object>("Order not found", HttpStatus.NOT_FOUND);

    }

}
