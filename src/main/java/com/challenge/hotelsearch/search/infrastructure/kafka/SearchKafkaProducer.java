package com.challenge.hotelsearch.search.infrastructure.kafka;

import com.challenge.hotelsearch.search.application.port.SearchEventPublisher;
import com.challenge.hotelsearch.search.domain.model.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchKafkaProducer implements SearchEventPublisher {

    private final KafkaTemplate<String, Search> kafkaTemplate;
    private static final String TOPIC = "hotel_availability_searches";

    @Override
    public void publish(Search search) {

        kafkaTemplate.send(TOPIC, search.getSearchId(), search);
    }
}