package com.challenge.hotelsearch.search.infrastructure.kafka;

import com.challenge.hotelsearch.search.application.service.SaveSearchService;
import com.challenge.hotelsearch.search.domain.model.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSearchConsumer {

    private final SaveSearchService saveSearchService;


    @KafkaListener(
            topics = "hotel_availability_searches"
    )
    public void listen(Search request) {
        saveSearchService.save(request);
    }
}
