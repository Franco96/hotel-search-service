package com.challenge.hotelsearch.search.infrastructure.kafka;

import com.challenge.hotelsearch.search.application.exception.SearchEventPublishException;
import com.challenge.hotelsearch.search.application.port.out.SearchEventPublisherPort;
import com.challenge.hotelsearch.search.domain.model.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SearchKafkaProducerAdapter implements SearchEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(SearchKafkaProducerAdapter.class);

    private final KafkaTemplate<String, Search> kafkaTemplate;
    private static final String TOPIC = "hotel_availability_searches";

    public SearchKafkaProducerAdapter(KafkaTemplate<String, Search> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Search search) {
        try {
            kafkaTemplate.send(TOPIC, search.searchId(), search).get();
        } catch (Exception ex) {
            log.error("Failed to publish search event [searchId={}]: {}", search.searchId(), ex.getMessage());
            throw new SearchEventPublishException("Could not publish search event");
        }
    }
}