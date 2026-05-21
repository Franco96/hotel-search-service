package com.challenge.hotelsearch.search.infrastructure.kafka;

import com.challenge.hotelsearch.search.application.port.in.SaveSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumer {

    private final SaveSearchUseCase saveSearchUseCase;

    public KafkaSearchConsumer(SaveSearchUseCase saveSearchUseCase) {
        this.saveSearchUseCase = saveSearchUseCase;
    }

    @KafkaListener(
            topics = "hotel_availability_searches",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(Search request) {
        saveSearchUseCase.save(request);
    }
}
