package com.challenge.hotelsearch.service;

import com.challenge.hotelsearch.search.application.port.SearchEventPublisher;
import com.challenge.hotelsearch.search.application.service.SearchRequestService;
import com.challenge.hotelsearch.search.domain.model.Search;
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
    private SearchEventPublisher searchEventPublisher;

    @InjectMocks
    private SearchRequestService service;

    @Test
    void shouldAssignSearchIdPublishAndReturnResponse() {
        Search search = Search.builder()
                .hotelId("123")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("10,70")
                .build();

        String searchId = service.createSearch(search);

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(searchEventPublisher).publish(captor.capture());
        Search published = captor.getValue();

        assertAll(
            () -> assertNotNull(searchId),
            () -> assertEquals(searchId, published.getSearchId()),
            () -> assertEquals("123", published.getHotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), published.getCheckIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), published.getCheckOut()),
            () -> assertEquals("10,70", published.getAges())
        );
    }
}
