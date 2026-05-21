package com.challenge.hotelsearch.kafka;

import com.challenge.hotelsearch.search.application.port.in.SaveSearchUseCase;
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
    private SaveSearchUseCase saveSearchUseCase;

    @InjectMocks
    private KafkaSearchConsumer consumer;

    @Test
    void shouldExecuteSaveInExecutorAndCallService() {
        Search search = new Search(
                "search-1",
                null,
                "123",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,70"
        );

        consumer.listen(search);

        verify(saveSearchUseCase).save(search);
    }
}
