package org.example.web;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.AnalyticsDashboardDTO;
import org.example.service.AnalyticsService;

import java.io.IOException;

@WebServlet("/admin/analytics")
public class AdminAnalyticsServlet extends HttpServlet {
    private final AnalyticsService analyticsService = new AnalyticsService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AnalyticsDashboardDTO dashboardData = analyticsService.getDashboardData();

            // Convert the Java object to a JSON string to be used by JavaScript
            String dashboardDataJson = gson.toJson(dashboardData);

            req.setAttribute("dashboardDataJson", dashboardDataJson);
            req.setAttribute("pageTitle", "Analytics");

        } catch (Exception e) {
            req.setAttribute("error", "Error loading analytics data: " + e.getMessage());
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/analytics.jsp").forward(req, resp);
    }
}