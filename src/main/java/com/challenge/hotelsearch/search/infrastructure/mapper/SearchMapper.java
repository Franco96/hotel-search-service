package com.challenge.hotelsearch.search.infrastructure.mapper;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.infrastructure.persistence.entity.SearchJpaEntity;
import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.rest.response.SearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Mapping(target = "hash", ignore = true)
    @Mapping(target = "searchId", ignore = true)
    @Mapping(target = "ages", source = "ages", qualifiedByName = "listToString")
    Search toEntity(SearchCreatedRequest dto);

    @Mapping(target = "ages", source = "ages", qualifiedByName = "stringToList")
    @Mapping(target = "checkIn", source = "checkIn", qualifiedByName = "formatDate")
    @Mapping(target = "checkOut", source = "checkOut", qualifiedByName = "formatDate")
    SearchResponse toSearchResponseDTO(Search search);

    SearchJpaEntity toJpaEntity(Search search);

    Search toDomain(SearchJpaEntity entity);

    @Named("listToString")
    default String listToString(List<Integer> ages) {
        return ages.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Named("stringToList")
    default List<Integer> stringToList(String ages) {
        if (ages == null || ages.isBlank()) {
            return List.of();
        }
        return Arrays.stream(ages.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    @Named("parseDate")
    default LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    @Named("formatDate")
    default String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}
