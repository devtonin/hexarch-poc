package com.sensedia.opin.hexarch.infrastructure.aws;

import com.sensedia.opin.hexarch.domain.dto.OrderDto;
import com.sensedia.opin.hexarch.domain.messaging.OrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerImpl implements OrderProducer {

   private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducerImpl.class);

   @Value("${cloud.aws.queues.toPreparation.name}")
   private String queuePreparationName;

   @Autowired
   private QueueMessagingTemplate queueMessagingTemplate;

   @Override
   public void send(OrderDto messagePayload) {
      try{
         LOGGER.info("Generating event.");
         queueMessagingTemplate.convertAndSend(queuePreparationName, messagePayload);
         LOGGER.info("Event has been published in SQS.");
      } catch (Exception e) {
         LOGGER.error("Exception ocurred while generating event to sqs : {} and stacktrace ; {}", e.getMessage(), e);
      }
   }
}
