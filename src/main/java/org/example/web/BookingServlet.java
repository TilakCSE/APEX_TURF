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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles GET for booking form and POST for booking creation
 */
@WebServlet(name = "BookingServlet", urlPatterns = "/booking")
public class BookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final TurfDao turfDao = new TurfDao();
    private final SportDao sportDao = new SportDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("turfs", turfDao.findAllActive());
            req.setAttribute("sports", sportDao.findAllActive());
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load data: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String turfIdStr = req.getParameter("turfId");
        String sportIdStr = req.getParameter("sportId");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String startStr = req.getParameter("startTime");
        String endStr = req.getParameter("endTime");

        try {
            long turfId = Long.parseLong(turfIdStr);
            long sportId = Long.parseLong(sportIdStr);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime start = LocalDateTime.parse(startStr, fmt);
            LocalDateTime end = LocalDateTime.parse(endStr, fmt);

            long bookingId = bookingService.createBooking(turfId, sportId, name, email, phone, start, end);
            req.setAttribute("success", "Booking confirmed! ID: " + bookingId);
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
        } catch (SQLException ex) {
            req.setAttribute("error", "Database error: " + ex.getMessage());
        } catch (Exception ex) {
            req.setAttribute("error", "Unexpected error: " + ex.getMessage());
        }

        // reload lists for the form
        try {
            req.setAttribute("turfs", turfDao.findAllActive());
            req.setAttribute("sports", sportDao.findAllActive());
        } catch (Exception ignored) {}

        req.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(req, resp);
    }
}
