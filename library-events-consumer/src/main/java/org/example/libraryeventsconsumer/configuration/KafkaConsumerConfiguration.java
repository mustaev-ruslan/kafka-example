package org.example.libraryeventsconsumer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableKafka
@EnableRetry
@Slf4j
public class KafkaConsumerConfiguration {

    @Bean
    public ErrorHandler errorHandler() {
        return (ex, data) -> log.error("CustomKafkaErrorHandler. Error on record: {}", data, ex);
    }

}
