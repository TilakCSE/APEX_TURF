package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Booking;
import org.example.service.BookingService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/admin/bookings/export")
public class AdminBookingExportServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String turfIdStr = req.getParameter("turfFilter");
            String sportIdStr = req.getParameter("sportFilter");
            String dateFilter = req.getParameter("dateFilter");
            Long turfId = (turfIdStr != null && !turfIdStr.isEmpty()) ? Long.parseLong(turfIdStr) : null;
            Long sportId = (sportIdStr != null && !sportIdStr.isEmpty()) ? Long.parseLong(sportIdStr) : null;

            List<Booking> bookings = bookingService.getFilteredBookings(turfId, sportId, dateFilter);

            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition", "attachment; filename=\"bookings_export.csv\"");

            PrintWriter writer = resp.getWriter();
            // CSV Header
            writer.println("BookingID,UserName,TurfName,SportName,StartTime,EndTime,Status");
            // CSV Data
            for (Booking b : bookings) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",%s,%s,%s%n",
                        b.getId(),
                        b.getUserName(),
                        b.getTurfName(),
                        b.getSportName(),
                        b.getStartTime().toString(),
                        b.getEndTime().toString(),
                        b.getStatus()
                );
            }
        } catch (Exception e) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Error exporting data: " + e.getMessage());
        }
    }
}