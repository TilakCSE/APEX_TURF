package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.TurfDao;
import org.example.model.Turf;
import org.example.service.ReviewService;

import java.io.IOException;

@WebServlet("/turf-details")
public class TurfDetailsServlet extends HttpServlet {
    private final TurfDao turfDao = new TurfDao();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String turfIdStr = req.getParameter("turfId");

        // --- Input Validation ---
        if (turfIdStr == null || turfIdStr.trim().isEmpty()) {
            req.getSession().setAttribute("error", "Invalid turf selected.");
            resp.sendRedirect(req.getContextPath() + "/booking");
            return;
        }

        try {
            long turfId = Long.parseLong(turfIdStr);
            Turf turf = turfDao.findById(turfId);

            if (turf == null) {
                // Handle case where turf ID does not exist in the database
                req.getSession().setAttribute("error", "The requested turf could not be found.");
                resp.sendRedirect(req.getContextPath() + "/booking");
                return;
            }

            // If everything is successful, fetch data and forward to the details page
            req.setAttribute("turf", turf);
            req.setAttribute("reviews", reviewService.getReviewsForTurf(turfId));
            req.setAttribute("averageRating", reviewService.getAverageRating(turfId));
            req.getRequestDispatcher("/WEB-INF/views/turf-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            // Handle case where turfId is not a valid number
            req.getSession().setAttribute("error", "Invalid turf ID format.");
            resp.sendRedirect(req.getContextPath() + "/booking");
        } catch (Exception e) {
            // For any other unexpected errors, show a proper error page
            e.printStackTrace();
            throw new ServletException("Error loading turf details for turfId=" + turfIdStr, e);
        }
    }
}