package com.devtonin.hexarch.application.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LogInterceptorConfiguration implements HandlerInterceptor {

   private static final String TRANSACTION_ID_HEADER_NAME = "x-transaction-id";
   private static final String TRANSACTION_ID_LOG_VAR_NAME = "transactionId";

   @Override
   public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
      final String transactionId = getTransactionIdFromHeader(request);
      MDC.put(TRANSACTION_ID_LOG_VAR_NAME, transactionId);
      return true;
   }

   @Override
   public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
      MDC.remove(TRANSACTION_ID_LOG_VAR_NAME);
   }

   private String getTransactionIdFromHeader(final HttpServletRequest request) {
      String transactionId = request.getHeader(TRANSACTION_ID_HEADER_NAME);
      if(StringUtils.isBlank(transactionId)){
         transactionId = generateUniqueId();
      }
      return transactionId;
   }

   private String generateUniqueId() {
      return UUID.randomUUID().toString();
   }
}
