package com.company.emailnotification.handler;

import com.company.core.kafka.ProductCreatedEvent;
import com.company.emailnotification.exception.NotRetryableException;
import com.company.emailnotification.exception.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handleProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {

        LOGGER.info("******** Received a new event: " + productCreatedEvent.getTitle());

        // just for simulate exception
        String requestUrl = "http://localhost:8082/response/500";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                LOGGER.info("******** Received response from a remote service: " + response.getBody());
            }
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RetryableException(e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NotRetryableException(e);
        }
    }
}
