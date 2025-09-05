package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Booking;

import javax.sql.DataSource;
import java.sql.*;

/**
 * DAO for Booking entity
 */
public class BookingDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public boolean isSlotAvailable(long turfId, long sportId, Timestamp start, Timestamp end) throws SQLException {
        // A slot is available if there is no overlapping CONFIRMED booking
        String sql = "SELECT COUNT(*) FROM bookings WHERE turf_id = ? AND sport_id = ? AND status = 'CONFIRMED' " +
                "AND NOT (end_time <= ? OR start_time >= ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, turfId);
            ps.setLong(2, sportId);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    public long create(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (user_id, turf_id, sport_id, start_time, end_time, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getTurfId());
            ps.setLong(3, booking.getSportId());
            ps.setTimestamp(4, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(5, Timestamp.valueOf(booking.getEndTime()));
            ps.setString(6, booking.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(booking.getCreatedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert booking, no ID returned");
    }
}
