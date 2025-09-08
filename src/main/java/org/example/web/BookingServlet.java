package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dao.SportDao;
import org.example.dao.TurfDao;
import org.example.model.User;
import org.example.service.BookingService;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "BookingServlet", urlPatterns = "/booking")
public class BookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final TurfDao turfDao = new TurfDao();
    private final SportDao sportDao = new SportDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // CORRECTED: Changed from findAllActive() to findAll()
            req.setAttribute("turfs", turfDao.findAll());
            req.setAttribute("sports", sportDao.findAllActive()); // SportDao still uses findAllActive
            req.setAttribute("currentPage", "booking");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load data: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String turfIdStr = req.getParameter("turfId");
        String sportIdStr = req.getParameter("sportId");
        String startStr = req.getParameter("startTime");
        String endStr = req.getParameter("endTime");

        try {
            long turfId = Long.parseLong(turfIdStr);
            long sportId = Long.parseLong(sportIdStr);
            LocalDateTime start = LocalDateTime.parse(startStr);
            LocalDateTime end = LocalDateTime.parse(endStr);

            long bookingId = bookingService.createBooking(turfId, sportId, user.getId(), start, end);
            req.setAttribute("success", "Booking confirmed! Your Booking ID is: " + bookingId);
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }

        doGet(req, resp);
    }
}