package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Turf;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Turf entity
 */
public class TurfDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public List<Turf> findAllActive() throws SQLException {
        String sql = "SELECT id, name, location, active FROM turfs WHERE active = 1 ORDER BY name";
        List<Turf> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Turf t = new Turf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBoolean("active")
                );
                list.add(t);
            }
        }
        return list;
    }
}
