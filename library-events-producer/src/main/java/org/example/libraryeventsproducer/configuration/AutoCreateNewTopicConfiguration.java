package org.example.libraryeventsproducer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("local")
public class AutoCreateNewTopicConfiguration {

    @Value("${spring.kafka.template.default-topic}")
    private String defaultTopic;

    @Bean
    public NewTopic libraryEvents() {
        return TopicBuilder.name(defaultTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }

}
