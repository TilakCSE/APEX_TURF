package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Sport;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Sport entity
 */
public class SportDao {
    private final DataSource dataSource = DatabaseConfig.getDataSource();

    public List<Sport> findAllActive() throws SQLException {
        String sql = "SELECT id, name, active FROM sports WHERE active = 1 ORDER BY name";
        List<Sport> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sport s = new Sport(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBoolean("active")
                );
                list.add(s);
            }
        }
        return list;
    }
}
