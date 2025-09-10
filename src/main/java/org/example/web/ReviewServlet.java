package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.BookingDao;
import org.example.model.Booking;
import org.example.model.Review;
import org.example.model.User;
import org.example.service.ReviewService;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();
    private final BookingDao bookingDao = new BookingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long bookingId = Long.parseLong(req.getParameter("bookingId"));
            Booking booking = bookingDao.findById(bookingId);
            User user = (User) req.getSession().getAttribute("user");

            // Security check: ensure user owns this booking
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                resp.sendRedirect(req.getContextPath() + "/my-bookings?error=not_authorized");
                return;
            }

            req.setAttribute("booking", booking);
            req.getRequestDispatcher("/WEB-INF/views/review-form.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/my-bookings?error=invalid_booking");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            long bookingId = Long.parseLong(req.getParameter("bookingId"));
            long turfId = Long.parseLong(req.getParameter("turfId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            Review review = new Review();
            review.setBookingId(bookingId);
            review.setTurfId(turfId);
            review.setUserId(user.getId());
            review.setRating(rating);
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());

            reviewService.submitReview(review);
            resp.sendRedirect(req.getContextPath() + "/my-bookings?success=review_submitted");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Error submitting review: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/my-bookings");
        }
    }
}