package com.challenge.hotelsearch.kafka;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.kafka.SearchKafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Search> kafkaTemplate;

    @InjectMocks
    private SearchKafkaProducer adapter;

    @Test
    void shouldPublishSearchToKafkaTopic() {
        Search search = Search.builder()
                .searchId("search-1")
                .hotelId("hotel-1")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("30,25")
                .build();

        adapter.publish(search);

        verify(kafkaTemplate).send("hotel_availability_searches", "search-1", search);
    }
}
