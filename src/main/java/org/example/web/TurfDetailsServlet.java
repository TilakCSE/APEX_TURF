package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.TurfDao;
import org.example.service.ReviewService;

import java.io.IOException;

@WebServlet("/turf-details")
public class TurfDetailsServlet extends HttpServlet {
    private final TurfDao turfDao = new TurfDao();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long turfId = Long.parseLong(req.getParameter("turfId"));
            req.setAttribute("turf", turfDao.findById(turfId));
            req.setAttribute("reviews", reviewService.getReviewsForTurf(turfId));
            req.setAttribute("averageRating", reviewService.getAverageRating(turfId));
            req.getRequestDispatcher("/WEB-INF/views/turf-details.jsp").forward(req, resp);
        } catch (Exception e) {
            // Redirect to a generic error page or home page
            resp.sendRedirect(req.getContextPath() + "/booking");
        }
    }
}