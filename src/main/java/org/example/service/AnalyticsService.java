package org.example.service;

import org.example.dao.AnalyticsDao;
import org.example.model.AnalyticsDashboardDTO;
import java.sql.SQLException;

public class AnalyticsService {
    private final AnalyticsDao analyticsDao = new AnalyticsDao();

    public AnalyticsDashboardDTO getDashboardData() throws SQLException {
        AnalyticsDashboardDTO dto = new AnalyticsDashboardDTO();

        dto.setTotalBookings(analyticsDao.getTotalBookings());
        dto.setPopularTurfs(analyticsDao.getPopularTurfs());
        dto.setPopularSports(analyticsDao.getPopularSports());
        dto.setPeakTimeUsage(analyticsDao.getPeakTimeUsage());
        dto.setBookingsPerDay(analyticsDao.getBookingsPerDay());

        // Placeholder for revenue logic. You could calculate this based on booking duration, etc.
        dto.setTotalRevenue(dto.getTotalBookings() * 500.0); // Example: 500 currency per booking

        return dto;
    }
}