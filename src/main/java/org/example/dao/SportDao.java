package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Sport;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public void create(Sport sport) throws SQLException {
        String sql = "INSERT INTO sports (name, active) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sport.getName());
            ps.setBoolean(2, sport.isActive());
            ps.executeUpdate();
        }
    }

    public Sport findById(long id) throws SQLException {
        String sql = "SELECT id, name, active FROM sports WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sport(rs.getLong("id"), rs.getString("name"), rs.getBoolean("active"));
                }
            }
        }
        return null;
    }

    public List<Sport> findAll() throws SQLException {
        List<Sport> list = new ArrayList<>();
        String sql = "SELECT id, name, active FROM sports ORDER BY name";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Sport(rs.getLong("id"), rs.getString("name"), rs.getBoolean("active")));
            }
        }
        return list;
    }

    // This method is still used by the user-facing booking form
    public List<Sport> findAllActive() throws SQLException {
        List<Sport> list = new ArrayList<>();
        String sql = "SELECT id, name, active FROM sports WHERE active = 1 ORDER BY name";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Sport(rs.getLong("id"), rs.getString("name"), rs.getBoolean("active")));
            }
        }
        return list;
    }

    public void update(Sport sport) throws SQLException {
        String sql = "UPDATE sports SET name = ?, active = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sport.getName());
            ps.setBoolean(2, sport.isActive());
            ps.setLong(3, sport.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        // Note: This is a hard delete. It will fail if sports are referenced elsewhere without ON DELETE CASCADE.
        String sql = "DELETE FROM sports WHERE id = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}