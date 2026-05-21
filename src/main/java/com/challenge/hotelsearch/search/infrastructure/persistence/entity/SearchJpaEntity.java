package com.challenge.hotelsearch.search.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "search", indexes = {
        @Index(name = "idx_hash", columnList = "hash")
})
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

    public String getSearchId() { return searchId; }
    public void setSearchId(String searchId) { this.searchId = searchId; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getHotelId() { return hotelId; }
    public void setHotelId(String hotelId) { this.hotelId = hotelId; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public String getAges() { return ages; }
    public void setAges(String ages) { this.ages = ages; }
}
