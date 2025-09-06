package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Booking;

import java.util.ArrayList;
import java.util.List;

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

    public Booking findById(long bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBooking(rs);
                }
            }
        }
        return null;
    }

    public List<Booking> findBookingsByUserEmail(String email) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, t.name as turf_name, s.name as sport_name " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN turfs t ON b.turf_id = t.id " +
                "JOIN sports s ON b.sport_id = s.id " +
                "WHERE u.email = ? " +
                "ORDER BY b.start_time DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapRowToBooking(rs);
                    booking.setTurfName(rs.getString("turf_name"));
                    booking.setSportName(rs.getString("sport_name"));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    public boolean updateStatus(long bookingId, long userId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setLong(2, bookingId);
            ps.setLong(3, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Booking mapRowToBooking(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("turf_id"),
                rs.getLong("sport_id"),
                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getTimestamp("end_time").toLocalDateTime(),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
