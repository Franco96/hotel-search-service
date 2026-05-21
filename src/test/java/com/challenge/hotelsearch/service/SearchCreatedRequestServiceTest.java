package com.challenge.hotelsearch.service;

import com.challenge.hotelsearch.search.application.port.out.SearchEventPublisherPort;
import com.challenge.hotelsearch.search.application.service.SearchRequestService;
import com.challenge.hotelsearch.search.domain.model.Search;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchCreatedRequestServiceTest {

    @Mock
    private SearchEventPublisherPort searchEventPublisherPort;

    @InjectMocks
    private SearchRequestService service;

    @Test
    void shouldAssignSearchIdPublishAndReturnResponse() {
        Search search = new Search(
                null,
                null,
                "123",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,70"
        );

        String searchId = service.createSearch(search);

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(searchEventPublisherPort).publish(captor.capture());
        Search published = captor.getValue();

        assertAll(
            () -> assertNotNull(searchId),
            () -> assertDoesNotThrow(() -> UUID.fromString(searchId)),
            () -> assertEquals(searchId, published.searchId()),
            () -> assertEquals("123", published.hotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), published.checkIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), published.checkOut()),
            () -> assertEquals("10,70", published.ages())
        );
    }
}
