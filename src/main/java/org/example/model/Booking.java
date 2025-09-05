package org.example.model;

import java.time.LocalDateTime;

/**
 * Represents a booking made by a user for a turf and sport within a time window.
 */
public class Booking {
    private Long id;
    private Long userId;
    private Long turfId;
    private Long sportId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // e.g., CONFIRMED, CANCELLED
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(Long id, Long userId, Long turfId, Long sportId, LocalDateTime startTime, LocalDateTime endTime, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.turfId = turfId;
        this.sportId = sportId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public Long getSportId() { return sportId; }
    public void setSportId(Long sportId) { this.sportId = sportId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
