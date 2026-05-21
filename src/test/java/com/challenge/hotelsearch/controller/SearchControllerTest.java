package com.challenge.hotelsearch.controller;

import com.challenge.hotelsearch.search.application.exception.SearchEventPublishException;
import com.challenge.hotelsearch.search.application.port.in.CreateSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.exception.RestExceptionHandler;
import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import com.challenge.hotelsearch.search.infrastructure.rest.controller.SearchController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SearchControllerTest {

    private final SearchMapper searchMapper = mock(SearchMapper.class);
    private final CreateSearchUseCase createSearchUseCase = mock(CreateSearchUseCase.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new SearchController(searchMapper, createSearchUseCase))
            .setControllerAdvice(new RestExceptionHandler())
            .build();

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @Test
    void shouldCreateSearch() throws Exception {

        SearchCreatedRequest request = new SearchCreatedRequest(
                "123ABc",
                LocalDate.of(2027, 1, 29),
                LocalDate.of(2027, 1, 31),
                List.of(10, 70)
        );

        Search entity = new Search(
                null,
                null,
                "123ABc",
                LocalDate.of(2027, 1, 29),
                LocalDate.of(2027, 1, 31),
                "10,70"
        );

        when(searchMapper.toEntity(any(SearchCreatedRequest.class))).thenReturn(entity);
        when(createSearchUseCase.createSearch(entity)).thenReturn("search-1");

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.searchId").value("search-1"));

        verify(searchMapper).toEntity(any(SearchCreatedRequest.class));
        verify(createSearchUseCase).createSearch(entity);
    }

    @Test
    void shouldReturn503WhenKafkaFails() throws Exception {
        SearchCreatedRequest request = new SearchCreatedRequest(
                "123ABc",
                LocalDate.of(2027, 1, 29),
                LocalDate.of(2027, 1, 31),
                List.of(10, 70)
        );

        when(searchMapper.toEntity(any(SearchCreatedRequest.class)))
                .thenReturn(new Search(null, null, "123ABc", LocalDate.of(2027, 1, 29), LocalDate.of(2027, 1, 31), "10,70"));
        when(createSearchUseCase.createSearch(any()))
                .thenThrow(new SearchEventPublishException("Kafka unavailable"));

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.title").value("Event publishing failed"))
                .andExpect(jsonPath("$.detail").value("Kafka unavailable"));
    }
}
