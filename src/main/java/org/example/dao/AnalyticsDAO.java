package org.example.dao;

import org.example.config.DatabaseConfig;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public Map<String, Long> getPopularTurfs() throws SQLException {
        Map<String, Long> data = new LinkedHashMap<>();
        String sql = "SELECT t.name, COUNT(b.id) as booking_count " +
                "FROM bookings b JOIN turfs t ON b.turf_id = t.id " +
                "GROUP BY t.name ORDER BY booking_count DESC LIMIT 5";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getLong("booking_count"));
            }
        }
        return data;
    }

    public Map<String, Long> getPopularSports() throws SQLException {
        Map<String, Long> data = new LinkedHashMap<>();
        String sql = "SELECT s.name, COUNT(b.id) as booking_count " +
                "FROM bookings b JOIN sports s ON b.sport_id = s.id " +
                "GROUP BY s.name ORDER BY booking_count DESC LIMIT 5";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getLong("booking_count"));
            }
        }
        return data;
    }

    public Map<String, Long> getPeakTimeUsage() throws SQLException {
        Map<String, Long> data = new LinkedHashMap<>();
        String sql = "SELECT HOUR(start_time) as hour_of_day, COUNT(*) as booking_count " +
                "FROM bookings GROUP BY hour_of_day ORDER BY hour_of_day ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int hour = rs.getInt("hour_of_day");
                String formattedHour = (hour == 0) ? "12 AM" : (hour < 12) ? hour + " AM" : (hour == 12) ? "12 PM" : (hour - 12) + " PM";
                data.put(formattedHour, rs.getLong("booking_count"));
            }
        }
        return data;
    }

    public Map<String, Long> getBookingsPerDay() throws SQLException {
        Map<String, Long> data = new LinkedHashMap<>();
        // Fetches data for the last 30 days
        String sql = "SELECT DATE(start_time) as booking_date, COUNT(*) as booking_count " +
                "FROM bookings WHERE start_time >= CURDATE() - INTERVAL 30 DAY " +
                "GROUP BY booking_date ORDER BY booking_date ASC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("booking_date"), rs.getLong("booking_count"));
            }
        }
        return data;
    }

    public long getTotalBookings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }
}