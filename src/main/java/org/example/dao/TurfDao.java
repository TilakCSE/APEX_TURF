package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Sport;
import org.example.model.Turf;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurfDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    // CREATE
    public void create(Turf turf, List<Long> sportIds) throws SQLException {
        String sqlTurf = "INSERT INTO turfs (name, location, active) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into turfs table
            PreparedStatement psTurf = conn.prepareStatement(sqlTurf, Statement.RETURN_GENERATED_KEYS);
            psTurf.setString(1, turf.getName());
            psTurf.setString(2, turf.getLocation());
            psTurf.setBoolean(3, turf.isActive());
            psTurf.executeUpdate();

            // Get the newly created turf ID
            ResultSet rs = psTurf.getGeneratedKeys();
            if (rs.next()) {
                long turfId = rs.getLong(1);
                // Insert into the turf_sports join table
                if (sportIds != null && !sportIds.isEmpty()) {
                    addSportsToTurf(conn, turfId, sportIds);
                }
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // Rollback on error
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // READ ONE
    public Turf findById(long id) throws SQLException {
        String sql = "SELECT id, name, location, active FROM turfs WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Turf(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getBoolean("active")
                    );
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<Turf> findAll() throws SQLException {
        String sql = "SELECT id, name, location, active FROM turfs ORDER BY name";
        List<Turf> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Turf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBoolean("active")
                ));
            }
        }
        return list;
    }

    // UPDATE
    public void update(Turf turf, List<Long> sportIds) throws SQLException {
        String sqlTurf = "UPDATE turfs SET name = ?, location = ?, active = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update the turfs table
            PreparedStatement psTurf = conn.prepareStatement(sqlTurf);
            psTurf.setString(1, turf.getName());
            psTurf.setString(2, turf.getLocation());
            psTurf.setBoolean(3, turf.isActive());
            psTurf.setLong(4, turf.getId());
            psTurf.executeUpdate();

            // Update the turf_sports associations
            removeSportsFromTurf(conn, turf.getId()); // Clear existing sports
            if (sportIds != null && !sportIds.isEmpty()) {
                addSportsToTurf(conn, turf.getId(), sportIds); // Add new sports
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // DELETE
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM turfs WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // HELPER METHODS for many-to-many relationship
    public List<Long> findSportIdsForTurf(long turfId) throws SQLException {
        String sql = "SELECT sport_id FROM turf_sports WHERE turf_id = ?";
        List<Long> sportIds = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, turfId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sportIds.add(rs.getLong("sport_id"));
                }
            }
        }
        return sportIds;
    }

    private void addSportsToTurf(Connection conn, long turfId, List<Long> sportIds) throws SQLException {
        String sql = "INSERT INTO turf_sports (turf_id, sport_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Long sportId : sportIds) {
                ps.setLong(1, turfId);
                ps.setLong(2, sportId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void removeSportsFromTurf(Connection conn, long turfId) throws SQLException {
        String sql = "DELETE FROM turf_sports WHERE turf_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, turfId);
            ps.executeUpdate();
        }
    }
}