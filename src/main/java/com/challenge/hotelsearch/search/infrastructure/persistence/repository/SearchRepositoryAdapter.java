package com.challenge.hotelsearch.search.infrastructure.persistence.repository;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.application.port.out.SearchRepositoryPort;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SearchRepositoryAdapter implements SearchRepositoryPort {

    private final SearchMapper mapper;
    private final JpaSearchRepository jpaRepository;

    public SearchRepositoryAdapter(SearchMapper mapper, JpaSearchRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

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
