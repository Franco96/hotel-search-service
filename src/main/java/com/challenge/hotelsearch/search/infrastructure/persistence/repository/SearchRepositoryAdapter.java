package com.challenge.hotelsearch.search.infrastructure.persistence.repository;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.domain.repository.SearchRepository;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryAdapter implements SearchRepository {

    private final SearchMapper mapper;
    private final JpaSearchRepository jpaRepository;

    @Override
    public void save(Search search) {
        jpaRepository.save(mapper.toJpaEntity(search));
    }

    @Override
    public Optional<Search> findBySearchId(String searchId) {
        return jpaRepository.findById(searchId).map(mapper::toDomain);
    }

    @Override
    public long countByHash(String hash) {
        return jpaRepository.countByHash(hash);
    }
}
