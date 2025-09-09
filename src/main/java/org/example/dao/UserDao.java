package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.User;

import javax.sql.DataSource;
import java.sql.*;

/**
 * DAO for User entity. For MVP we create/get by email if necessary.
 */
public class UserDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, phone, password, role FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public long create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword()); // Add password
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { return rs.getLong(1); }
            }
        }
        throw new SQLException("Failed to insert user, no ID returned");
    }

    public User findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, phone, password, role FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}

