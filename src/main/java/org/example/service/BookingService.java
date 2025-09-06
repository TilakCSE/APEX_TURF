package org.example.service;

import org.example.dao.BookingDao;
import org.example.dao.UserDao;
import org.example.model.Booking;
import org.example.model.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BookingService contains the core booking logic and time slot validation rules.
 */
public class BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private static final int CANCELLATION_CUTOFF_HOURS = 12;

    public BookingService() {
        this.bookingDao = new BookingDao();
        this.userDao = new UserDao();
    }

    /**
     * Validates the requested time slot and creates a booking if available.
     * Basic rules:
     * - Start < End
     * - Minimum duration 30 minutes, maximum 4 hours
     * - Start time not in the past
     * - No overlap with existing confirmed bookings on same turf+sport
     */
    public long createBooking(long turfId, long sportId, String userName, String userEmail, String userPhone,
                              LocalDateTime start, LocalDateTime end) throws SQLException {
        validateTimes(start, end);

        // Check availability
        boolean available = bookingDao.isSlotAvailable(turfId, sportId, Timestamp.valueOf(start), Timestamp.valueOf(end));
        if (!available) {
            throw new IllegalArgumentException("Selected time slot is not available. Please choose another slot.");
        }

        // Ensure user exists or create
        Long userId;
        User existing = userDao.findByEmail(userEmail);
        if (existing == null) {
            User u = new User(null, userName, userEmail, userPhone);
            userId = userDao.create(u);
        } else {
            userId = existing.getId();
        }

        Booking booking = new Booking(null, userId, turfId, sportId, start, end, "CONFIRMED", LocalDateTime.now());
        return bookingDao.create(booking);
    }

    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and End time are required");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time cannot be in the past");
        }
        Duration d = Duration.between(start, end);
        if (d.toMinutes() < 30) {
            throw new IllegalArgumentException("Minimum booking duration is 30 minutes");
        }
        if (d.toHours() > 4) {
            throw new IllegalArgumentException("Maximum booking duration is 4 hours");
        }
    }

    public List<Booking> getBookingsForUser(String email) throws SQLException {
        List<Booking> bookings = bookingDao.findBookingsByUserEmail(email);
        // Set the transient 'cancellable' flag based on business rules
        for (Booking booking : bookings) {
            boolean isCancellable = "CONFIRMED".equals(booking.getStatus()) &&
                    LocalDateTime.now().isBefore(booking.getStartTime().minusHours(CANCELLATION_CUTOFF_HOURS));
            booking.setCancellable(isCancellable);
        }
        return bookings;
    }

    public void cancelBooking(long bookingId, String userEmail) throws SQLException, SecurityException {
        User user = userDao.findByEmail(userEmail);
        if (user == null) {
            throw new SecurityException("User not found.");
        }

        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found.");
        }

        // Security check: ensure the user owns this booking
        if (!booking.getUserId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to cancel this booking.");
        }

        // Business rule check: status must be confirmed
        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new IllegalStateException("This booking cannot be cancelled as it is not confirmed.");
        }

        // Business rule check: must be before the cutoff time
        if (LocalDateTime.now().isAfter(booking.getStartTime().minusHours(CANCELLATION_CUTOFF_HOURS))) {
            throw new IllegalStateException("Cancellation is only allowed up to " + CANCELLATION_CUTOFF_HOURS + " hours before the booking time.");
        }

        // All checks passed, proceed to cancel
        bookingDao.updateStatus(bookingId, user.getId(), "CANCELLED");
    }
}
