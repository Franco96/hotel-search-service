package com.challenge.hotelsearch.repository;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import com.challenge.hotelsearch.search.infrastructure.persistence.repository.JpaSearchRepository;
import com.challenge.hotelsearch.search.infrastructure.persistence.entity.SearchJpaEntity;
import com.challenge.hotelsearch.search.infrastructure.persistence.repository.SearchRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SearchRepositoryAdapterTest {

    @Mock
    private SearchMapper mapper;

    @Mock
    private JpaSearchRepository jpaRepository;

    @InjectMocks
    private SearchRepositoryAdapter adapter;

    private Search buildSearch() {
        return new Search(
                "s1",
                "hash-1",
                "h1",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,20"
        );
    }

    private SearchJpaEntity buildEntity() {
        SearchJpaEntity entity = new SearchJpaEntity();
        entity.setSearchId("s1");
        entity.setHash("hash-1");
        entity.setHotelId("h1");
        entity.setCheckIn(LocalDate.of(2026, 4, 20));
        entity.setCheckOut(LocalDate.of(2026, 4, 27));
        entity.setAges("10,20");
        return entity;
    }

    @Test
    void shouldDelegateSaveToJpa() {
        Search search = buildSearch();
        SearchJpaEntity entity = buildEntity();

        when(mapper.toJpaEntity(search)).thenReturn(entity);

        adapter.save(search);

        verify(mapper).toJpaEntity(search);
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldFindBySearchId() {
        SearchJpaEntity entity = buildEntity();
        Search search = buildSearch();

        when(jpaRepository.findById("s1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(search);

        Search result = adapter.findBySearchId("s1").orElseThrow();

        assertAll(
                () -> assertEquals("s1", result.searchId()),
                () -> assertEquals("h1", result.hotelId()),
                () -> assertEquals(LocalDate.of(2026, 4, 20), result.checkIn()),
                () -> assertEquals("hash-1", result.hash())
        );

        verify(jpaRepository).findById("s1");
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenSearchNotFound() {
        when(jpaRepository.findById("missing")).thenReturn(Optional.empty());

        Optional<Search> result = adapter.findBySearchId("missing");

        assertTrue(result.isEmpty());

        verify(jpaRepository).findById("missing");
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldDelegateCountByHash() {
        when(jpaRepository.countByHash("hash-abc")).thenReturn(7L);

        long result = adapter.countByHash("hash-abc");

        assertEquals(7L, result);

        verify(jpaRepository).countByHash("hash-abc");
    }
}
