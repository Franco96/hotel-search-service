package com.challenge.hotelsearch.search.infrastructure.config;

import com.challenge.hotelsearch.search.application.port.out.SearchEventPublisherPort;
import com.challenge.hotelsearch.search.application.service.CountService;
import com.challenge.hotelsearch.search.application.service.SaveSearchService;
import com.challenge.hotelsearch.search.application.service.SearchRequestService;
import com.challenge.hotelsearch.search.application.port.in.CountSearchUseCase;
import com.challenge.hotelsearch.search.application.port.in.CreateSearchUseCase;
import com.challenge.hotelsearch.search.application.port.in.SaveSearchUseCase;
import com.challenge.hotelsearch.search.application.port.out.SearchRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public CreateSearchUseCase createSearchUseCase(SearchEventPublisherPort searchEventPublisherPort) {
        return new SearchRequestService(searchEventPublisherPort);
    }

    @Bean
    public CountSearchUseCase countSearchUseCase(SearchRepositoryPort searchRepositoryPort) {
        return new CountService(searchRepositoryPort);
    }

    @Bean
    public SaveSearchUseCase saveSearchUseCase(SearchRepositoryPort searchRepositoryPort) {
        return new SaveSearchService(searchRepositoryPort);
    }
}
