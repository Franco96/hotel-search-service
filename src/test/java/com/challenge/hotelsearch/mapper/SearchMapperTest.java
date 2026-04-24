package com.challenge.hotelsearch.mapper;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.rest.response.SearchResponse;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchMapperTest {

    private final SearchMapper mapper = Mappers.getMapper(SearchMapper.class);

    @Test
    void shouldMapRequestToEntity() {
        SearchCreatedRequest dto = new SearchCreatedRequest("123",  LocalDate.of(2026, 4, 20),  LocalDate.of(2026, 4, 27), List.of(10, 70));

        Search result = mapper.toEntity(dto);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("123", result.getHotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), result.getCheckIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), result.getCheckOut()),
            () -> assertEquals("10,70", result.getAges()),
            () -> assertNull(result.getSearchId()),
            () -> assertNull(result.getHash())
        );
    }

    @Test
    void shouldMapEntityToResponse() {
        Search search = Search.builder()
                .searchId("search-1")
                .hotelId("123")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("10,70")
                .hash("hash-123")
                .build();

        SearchResponse result = mapper.toSearchResponseDTO(search);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("123", result.hotelId()),
            () -> assertEquals("20/04/2026", result.checkIn()),
            () -> assertEquals("27/04/2026", result.checkOut()),
            () -> assertEquals(List.of(10, 70), result.ages())
        );
    }

    @Test
    void shouldMapAgesFromListToString() {
        assertEquals("1,2,3", mapper.listToString(List.of(1, 2, 3)));
    }

    @Test
    void shouldMapAgesReverseFromStringToList() {
        assertEquals(List.of(1, 2, 3), mapper.stringToList("1,2,3"));
    }

    @Test
    void shouldReturnEmptyListWhenAgesIsNullOrBlank() {
        assertAll(
            () -> assertEquals(List.of(), mapper.stringToList(null)),
            () -> assertEquals(List.of(), mapper.stringToList("")),
            () -> assertEquals(List.of(), mapper.stringToList("   "))
        );
    }
}
