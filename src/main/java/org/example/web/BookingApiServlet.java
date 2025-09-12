package org.example.web;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Booking;
import org.example.service.BookingService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/bookings")
public class BookingApiServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long turfId = Long.parseLong(req.getParameter("turfId"));
            // FullCalendar sends dates in ISO 8601 format, which LocalDateTime can parse directly
            LocalDateTime start = LocalDateTime.parse(req.getParameter("start"));
            LocalDateTime end = LocalDateTime.parse(req.getParameter("end"));

            List<Booking> bookings = bookingService.getBookingsForCalendar(turfId, start, end);

            // Convert the list of Booking objects into a format FullCalendar understands
            List<Map<String, String>> events = bookings.stream().map(booking -> Map.of(
                    "title", "Booked",
                    "start", booking.getStartTime().toString(),
                    "end", booking.getEndTime().toString()
            )).collect(Collectors.toList());

            // Set response type to JSON and write the data
            resp.setContentType("application/json");

            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(events));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching booking data.");
            e.printStackTrace();
        }
    }
}