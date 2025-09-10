package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.ReviewService;

import java.io.IOException;

@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("reviewList", reviewService.getAllReviews());
            req.setAttribute("pageTitle", "Reviews");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load reviews: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/reviews.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long reviewId = Long.parseLong(req.getParameter("reviewId"));
            reviewService.deleteReview(reviewId);
            req.getSession().setAttribute("successMessage", "Review #" + reviewId + " has been deleted.");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error deleting review: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/reviews");
    }
}