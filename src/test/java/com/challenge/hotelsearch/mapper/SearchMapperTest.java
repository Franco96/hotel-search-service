package com.challenge.hotelsearch.mapper;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.persistence.entity.SearchJpaEntity;
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
            () -> assertEquals("123", result.hotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), result.checkIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), result.checkOut()),
            () -> assertEquals("10,70", result.ages()),
            () -> assertNull(result.searchId()),
            () -> assertNull(result.hash())
        );
    }

    @Test
    void shouldMapEntityToResponse() {
        Search search = new Search(
                "search-1",
                "hash-123",
                "123",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,70"
        );

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

    @Test
    void shouldMapSearchToJpaEntity() {
        Search search = new Search(
                "s1",
                "hash-abc",
                "hotel-1",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,20"
        );

        SearchJpaEntity result = mapper.toJpaEntity(search);

        assertAll(
            () -> assertEquals("s1", result.getSearchId()),
            () -> assertEquals("hash-abc", result.getHash()),
            () -> assertEquals("hotel-1", result.getHotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), result.getCheckIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), result.getCheckOut()),
            () -> assertEquals("10,20", result.getAges())
        );
    }

    @Test
    void shouldMapJpaEntityToDomain() {
        SearchJpaEntity entity = new SearchJpaEntity();
        entity.setSearchId("s1");
        entity.setHash("hash-abc");
        entity.setHotelId("hotel-1");
        entity.setCheckIn(LocalDate.of(2026, 4, 20));
        entity.setCheckOut(LocalDate.of(2026, 4, 27));
        entity.setAges("10,20");

        Search result = mapper.toDomain(entity);

        assertAll(
            () -> assertEquals("s1", result.searchId()),
            () -> assertEquals("hash-abc", result.hash()),
            () -> assertEquals("hotel-1", result.hotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), result.checkIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), result.checkOut()),
            () -> assertEquals("10,20", result.ages())
        );
    }
}
