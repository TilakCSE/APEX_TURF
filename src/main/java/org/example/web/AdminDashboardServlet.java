package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.SportDao;
import org.example.dao.TurfDao;
import org.example.service.BookingService;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService();
    private final TurfDao turfDao = new TurfDao();
    private final SportDao sportDao = new SportDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Get filter parameters
            String turfIdStr = req.getParameter("turfFilter");
            String sportIdStr = req.getParameter("sportFilter");
            String dateFilter = req.getParameter("dateFilter");

            Long turfId = (turfIdStr != null && !turfIdStr.isEmpty()) ? Long.parseLong(turfIdStr) : null;
            Long sportId = (sportIdStr != null && !sportIdStr.isEmpty()) ? Long.parseLong(sportIdStr) : null;

            // Set attributes for the view
            req.setAttribute("pageTitle", "Dashboard");
            req.setAttribute("allBookings", bookingService.getFilteredBookings(turfId, sportId, dateFilter));
            req.setAttribute("allTurfs", turfDao.findAll());
            req.setAttribute("allSports", sportDao.findAll());

            // Persist filter values in the form
            req.setAttribute("selectedTurf", turfId);
            req.setAttribute("selectedSport", sportId);
            req.setAttribute("selectedDate", dateFilter);

        } catch (Exception e) {
            req.setAttribute("error", "Error loading dashboard data: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        long bookingId = Long.parseLong(req.getParameter("bookingId"));

        try {
            if ("cancel".equals(action)) {
                bookingService.updateBookingStatusAsAdmin(bookingId, "CANCELLED");
                req.getSession().setAttribute("successMessage", "Booking #" + bookingId + " has been cancelled.");
            } else if ("confirm".equals(action)) {
                bookingService.updateBookingStatusAsAdmin(bookingId, "CONFIRMED");
                req.getSession().setAttribute("successMessage", "Booking #" + bookingId + " has been re-confirmed.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error updating booking: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}