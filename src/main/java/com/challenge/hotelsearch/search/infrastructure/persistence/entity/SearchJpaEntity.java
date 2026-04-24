package com.challenge.hotelsearch.search.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "search", indexes = {
        @Index(name = "idx_hash", columnList = "hash")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchJpaEntity {

    @Id
    private String searchId;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String hotelId;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private String ages;
}
