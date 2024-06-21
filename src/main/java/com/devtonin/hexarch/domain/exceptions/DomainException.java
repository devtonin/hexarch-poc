package com.devtonin.hexarch.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class DomainException extends RuntimeException {
   private List<ErrorResponse> errorResponses;
}
