package com.challenge.hotelsearch.search.infrastructure.persistence.repository;

import com.challenge.hotelsearch.search.infrastructure.persistence.entity.SearchJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSearchRepository extends JpaRepository<SearchJpaEntity, String> {
    long countByHash(String hash);
}
