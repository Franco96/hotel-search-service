package com.challenge.hotelsearch.search.infrastructure.config;

import com.challenge.hotelsearch.search.domain.model.Search;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Search> kafkaListenerContainerFactory(
            ConsumerFactory<String, Search> consumerFactory,
            KafkaTemplate<String, Search> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, Search> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return factory;
    }

    private DefaultErrorHandler errorHandler(KafkaTemplate<String, Search> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    log.error("Sending to DLT [topic={}, offset={}]: {}",
                            record.topic(), record.offset(),
                            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                    return new TopicPartition(record.topic() + ".DLT", record.partition());
                });

        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 2L));
    }
}
