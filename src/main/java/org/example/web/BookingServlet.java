package org.example.web;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dao.SportDao;
import org.example.dao.TurfDao;
import org.example.model.Turf;
import org.example.model.User;
import org.example.service.BookingService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BookingServlet", urlPatterns = "/booking")
public class BookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final TurfDao turfDao = new TurfDao();
    private final SportDao sportDao = new SportDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Turf> turfs = turfDao.findAll();
            req.setAttribute("turfs", turfs);
            req.setAttribute("sports", sportDao.findAllActive());
            req.setAttribute("currentPage", "booking");

            // Pass the ID of the first turf in the list so the calendar can load its data initially
            if (turfs != null && !turfs.isEmpty()) {
                req.setAttribute("initialTurfId", turfs.get(0).getId());
            }

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load booking data: " + e.getMessage());
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(gson.toJson(Map.of("success", false, "message", "You must be logged in to book.")));
            return;
        }

        try {
            long turfId = Long.parseLong(req.getParameter("turfId"));
            long sportId = Long.parseLong(req.getParameter("sportId"));
            LocalDateTime start = LocalDateTime.parse(req.getParameter("startTime"));
            LocalDateTime end = LocalDateTime.parse(req.getParameter("endTime"));

            bookingService.createBooking(turfId, sportId, user.getId(), start, end);

            resp.getWriter().write(gson.toJson(Map.of("success", true, "message", "Booking confirmed!")));

        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("success", false, "message", ex.getMessage())));
            ex.printStackTrace();
        }
    }
}