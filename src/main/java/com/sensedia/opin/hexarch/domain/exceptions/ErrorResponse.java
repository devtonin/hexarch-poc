package com.sensedia.opin.hexarch.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Getter
@ToString
public class ErrorResponse {
   private String code;
   private String title;
   private String detail;
   private final Instant requestDateTime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
}
