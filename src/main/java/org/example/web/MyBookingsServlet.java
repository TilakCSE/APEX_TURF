package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Booking;
import org.example.service.BookingService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyBookingsServlet", urlPatterns = "/my-bookings")
public class MyBookingsServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/my-bookings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String email = req.getParameter("email");

        try {
            if ("lookup".equals(action)) {
                // Just lookup, no success/error message needed unless it fails
            } else if ("cancel".equals(action)) {
                long bookingId = Long.parseLong(req.getParameter("bookingId"));
                bookingService.cancelBooking(bookingId, email);
                req.setAttribute("successMessage", "Booking #" + bookingId + " has been cancelled successfully.");
            }

            // For both actions, we need to reload the booking list to show the latest state
            if (email != null && !email.isEmpty()) {
                List<Booking> bookings = bookingService.getBookingsForUser(email);
                req.setAttribute("bookings", bookings);
                req.setAttribute("lookupEmail", email); // To pre-fill the form
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
            // If an error occurs, we still want to show the existing list if possible
            try {
                if (email != null && !email.isEmpty()) {
                    List<Booking> bookings = bookingService.getBookingsForUser(email);
                    req.setAttribute("bookings", bookings);
                    req.setAttribute("lookupEmail", email);
                }
            } catch (Exception ignored) {}
        }

        req.getRequestDispatcher("/WEB-INF/views/my-bookings.jsp").forward(req, resp);
    }
}