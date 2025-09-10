package org.example.model;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private Long userId;
    private Long turfId;
    private Long bookingId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    // Extra field for display purposes
    private String userName;

    public Review() {}

    // Getters and Setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}