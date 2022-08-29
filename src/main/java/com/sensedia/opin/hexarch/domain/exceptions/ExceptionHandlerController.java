package com.sensedia.opin.hexarch.domain.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

   private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

   @ExceptionHandler(value = DomainException.class)
   public ResponseEntity<Object> handleAllDomainExceptions(DomainException domainException) {
      domainException.getErrorResponses().forEach(err -> log.error("Domain error: {}", err));
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(domainException.getErrorResponses());
   }
}
