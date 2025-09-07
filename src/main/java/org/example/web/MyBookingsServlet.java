package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.Booking;
import org.example.model.User;
import org.example.service.BookingService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyBookingsServlet", urlPatterns = "/my-bookings")
public class MyBookingsServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        req.setAttribute("currentPage", "my-bookings"); // <-- ADD THIS LINE

        try {
            List<Booking> bookings = bookingService.getBookingsForUser(user.getId());
            req.setAttribute("bookings", bookings);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error fetching your bookings: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/my-bookings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            long bookingId = Long.parseLong(req.getParameter("bookingId"));
            // CORRECTED: Cancel booking using the logged-in user's ID
            bookingService.cancelBooking(bookingId, user.getId());
            req.setAttribute("successMessage", "Booking #" + bookingId + " has been cancelled successfully.");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        // After processing, reload the page by calling the GET handler
        doGet(req, resp);
    }
}