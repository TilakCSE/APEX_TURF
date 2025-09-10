package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private Long id;
    private Long userId;
    private Long turfId;
    private Long sportId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime createdAt;
    private transient boolean isReviewable;


    // New fields for easier display in JSP
    private String turfName;
    private String sportName;
    private String userName;
    private transient boolean cancellable; // Not stored in DB

    // Constructors, standard getters/setters for old fields...

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


    public String getFormattedStartTime() {
        if (startTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        return this.startTime.format(formatter);
    }

    public String getFormattedEndTime() {
        if (endTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return this.endTime.format(formatter);
    }
    // Getters and setters for new fields
    public String getTurfName() { return turfName; }
    public void setTurfName(String turfName) { this.turfName = turfName; }
    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }
    public boolean isCancellable() { return cancellable; }
    public void setCancellable(boolean cancellable) { this.cancellable = cancellable; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public boolean isReviewable() { return isReviewable; }
    public void setReviewable(boolean reviewable) { this.isReviewable = reviewable; }
}