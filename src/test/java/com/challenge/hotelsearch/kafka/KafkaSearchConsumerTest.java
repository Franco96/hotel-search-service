package com.challenge.hotelsearch.kafka;

import com.challenge.hotelsearch.search.application.service.SaveSearchService;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.kafka.KafkaSearchConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaSearchConsumerTest {

    @Mock
    private SaveSearchService saveSearchService;

    @InjectMocks
    private KafkaSearchConsumer consumer;

    @Test
    void shouldExecuteSaveInExecutorAndCallService() {
        Search search = Search.builder()
                .searchId("search-1")
                .hotelId("123")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("10,70")
                .build();

        consumer.listen(search);

        verify(saveSearchService).save(search);
    }
}
