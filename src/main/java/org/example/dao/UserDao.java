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
        String sql = "SELECT id, name, email, phone FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    return u;
                }
            }
        }
        return null;
    }

    public long create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert user, no ID returned");
    }
}
