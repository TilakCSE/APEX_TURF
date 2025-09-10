package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Review;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public void create(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, turf_id, booking_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, review.getUserId());
            ps.setLong(2, review.getTurfId());
            ps.setLong(3, review.getBookingId());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setTimestamp(6, Timestamp.valueOf(review.getCreatedAt()));
            ps.executeUpdate();
        }
    }

    public List<Review> findByTurfId(long turfId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.name as user_name FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.turf_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, turfId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapRowToReview(rs));
                }
            }
        }
        return reviews;
    }

    public double getAverageRatingForTurf(long turfId) throws SQLException {
        String sql = "SELECT AVG(rating) FROM reviews WHERE turf_id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, turfId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    public boolean hasUserReviewedBooking(long bookingId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE booking_id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void delete(long reviewId) throws SQLException {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reviewId);
            ps.executeUpdate();
        }
    }

    public List<Review> findAll() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.name as user_name, t.name as turf_name FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN turfs t ON r.turf_id = t.id " +
                "ORDER BY r.created_at DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Review review = mapRowToReview(rs); // Your existing mapRowToReview should work
                review.setTurfName(rs.getString("turf_name")); // Set the new field
                reviews.add(review);
            }
        }
        return reviews;
    }

    private Review mapRowToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setUserId(rs.getLong("user_id"));
        review.setTurfId(rs.getLong("turf_id"));
        review.setBookingId(rs.getLong("booking_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Check if the joined column exists before trying to access it
        if (columnExists(rs, "user_name")) {
            review.setUserName(rs.getString("user_name"));
        }
        return review;
    }

    private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(metaData.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}