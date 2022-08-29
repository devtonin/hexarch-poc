package com.sensedia.opin.hexarch.infrastructure.aws.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@Configuration
public class SqsConfig {

   private static final Logger LOGGER = LoggerFactory.getLogger(SqsConfig.class);

   @Value("${cloud.aws.region.static}")
   private String region;

   @Value("${cloud.aws.queues.uri}")
   private String queueUri;

   @Bean
   public AmazonSQSAsync amazonSQS() {
      LOGGER.info("Creating Amazon SQS Async connection.");
      return AmazonSQSAsyncClientBuilder.standard()
              .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(queueUri, region))
              .withCredentials(new DefaultAWSCredentialsProviderChain())
              .build();
   }

   @Bean
   public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
      return new QueueMessagingTemplate(amazonSQSAsync);
   }

   @Bean
   public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper, final AmazonSQSAsync amazonSQSAsync){
      final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
      queueHandlerFactory.setAmazonSqs(amazonSQSAsync);
      queueHandlerFactory.setArgumentResolvers(Collections.singletonList(new PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))));
      return queueHandlerFactory;
   }

   private MessageConverter jackson2MessageConverter(final ObjectMapper mapper){
      final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
      converter.setObjectMapper(mapper);
      converter.setSerializedPayloadClass(String.class);
      converter.setStrictContentTypeMatch(false);
      return converter;
   }
}

