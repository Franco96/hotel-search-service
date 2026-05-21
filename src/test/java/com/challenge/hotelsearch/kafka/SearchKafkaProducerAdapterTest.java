package com.challenge.hotelsearch.kafka;

import com.challenge.hotelsearch.search.application.exception.SearchEventPublishException;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.kafka.SearchKafkaProducerAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchKafkaProducerAdapterTest {

    @Mock
    private KafkaTemplate<String, Search> kafkaTemplate;

    @InjectMocks
    private SearchKafkaProducerAdapter adapter;

    @Test
    void shouldPublishSearchToKafkaTopic() {
        Search search = new Search(
                "search-1",
                null,
                "hotel-1",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "30,25"
        );

        when(kafkaTemplate.send(anyString(), anyString(), any(Search.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        adapter.publish(search);

        verify(kafkaTemplate).send("hotel_availability_searches", "search-1", search);
    }

    @Test
    void shouldThrowWhenKafkaFails() {
        Search search = new Search(
                "search-1",
                null,
                "hotel-1",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "30,25"
        );

        CompletableFuture<Object> failed = new CompletableFuture<>();
        failed.completeExceptionally(new RuntimeException("Kafka unavailable"));
        when(kafkaTemplate.send(anyString(), anyString(), any(Search.class)))
                .thenReturn((CompletableFuture) failed);

        assertThrows(SearchEventPublishException.class, () -> adapter.publish(search));
    }
}
