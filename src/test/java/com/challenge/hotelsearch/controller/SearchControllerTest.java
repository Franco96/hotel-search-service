package com.challenge.hotelsearch.controller;

import com.challenge.hotelsearch.search.application.service.SearchRequestService;
import com.challenge.hotelsearch.search.domain.model.Search;
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
    private final SearchRequestService searchRequestService = mock(SearchRequestService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new SearchController(searchMapper, searchRequestService))
            .build();

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @Test
    void shouldCreateSearch() throws Exception {

        SearchCreatedRequest request = new SearchCreatedRequest(
                "123ABc",
                LocalDate.of(2026, 1, 29),
                LocalDate.of(2026, 1, 31),
                List.of(10, 70)
        );

        Search entity = Search.builder()
                .hotelId("123ABc")
                .checkIn(LocalDate.of(2026, 1, 29))
                .checkOut(LocalDate.of(2026, 1, 31))
                .ages("10,70")
                .build();

        when(searchMapper.toEntity(any(SearchCreatedRequest.class))).thenReturn(entity);
        when(searchRequestService.createSearch(entity)).thenReturn("search-1");

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("search-1"));

        verify(searchMapper).toEntity(any(SearchCreatedRequest.class));
        verify(searchRequestService).createSearch(entity);
    }
}
