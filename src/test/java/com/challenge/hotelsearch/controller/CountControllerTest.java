package com.challenge.hotelsearch.controller;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.application.port.in.CountSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.exception.RestExceptionHandler;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import com.challenge.hotelsearch.search.infrastructure.rest.controller.CountController;
import com.challenge.hotelsearch.search.infrastructure.rest.response.SearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CountControllerTest {

    private final CountSearchUseCase countSearchUseCase = mock(CountSearchUseCase.class);
    private final SearchMapper searchMapper = mock(SearchMapper.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new CountController(countSearchUseCase, searchMapper))
            .setControllerAdvice(new RestExceptionHandler())
            .build();

    @Test
    void shouldReturnCountResponse() throws Exception {
        Search search = new Search(
                "search-1",
                "somehash",
                "1234aBc",
                LocalDate.of(2026, 1, 29),
                LocalDate.of(2026, 1, 31),
                "30,29,1,3"
        );

        CountResultDTO countResult = new CountResultDTO("search-1", search, 4L);

        when(countSearchUseCase.count("search-1")).thenReturn(countResult);

        when(searchMapper.toSearchResponseDTO(any())).thenReturn(
                new SearchResponse(
                        "1234aBc",
                        "29/01/2026",
                        "31/01/2026",
                        List.of(30, 29, 1, 3)
                )
        );

        mockMvc.perform(get("/count").param("searchId", "search-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("search-1"))
                .andExpect(jsonPath("$.count").value(4))
                .andExpect(jsonPath("$.search.hotelId").value("1234aBc"))
                .andExpect(jsonPath("$.search.checkIn").value("29/01/2026"))
                .andExpect(jsonPath("$.search.checkOut").value("31/01/2026"))
                .andExpect(jsonPath("$.search.ages[0]").value(30))
                .andExpect(jsonPath("$.search.ages[1]").value(29))
                .andExpect(jsonPath("$.search.ages[2]").value(1))
                .andExpect(jsonPath("$.search.ages[3]").value(3));

        verify(countSearchUseCase).count("search-1");
        verify(searchMapper).toSearchResponseDTO(search);
    }

    @Test
    void shouldReturn404WhenSearchNotFound() throws Exception {
        when(countSearchUseCase.count("missing")).thenThrow(new SearchNotFoundException("Search not found"));

        mockMvc.perform(get("/count").param("searchId", "missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not found"))
                .andExpect(jsonPath("$.detail").value("Search not found"));
    }
}
