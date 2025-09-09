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
    private final EmailService emailService;
    private static final int CANCELLATION_CUTOFF_HOURS = 12;

    public BookingService() {
        this.bookingDao = new BookingDao();
        this.userDao = new UserDao();
        this.emailService = new EmailService();
    }

    /**
     * Validates the requested time slot and creates a booking if available.
     * Basic rules:
     * - Start < End
     * - Minimum duration 30 minutes, maximum 4 hours
     * - Start time not in the past
     * - No overlap with existing confirmed bookings on same turf+sport
     */
    public long createBooking(long turfId, long sportId, long userId, LocalDateTime start, LocalDateTime end) throws SQLException {
        // ... validation logic is the same ...
        validateTimes(start, end);
        boolean available = bookingDao.isSlotAvailable(turfId, sportId, Timestamp.valueOf(start), Timestamp.valueOf(end));
        if (!available) {
            throw new IllegalArgumentException("Selected time slot is not available.");
        }

        Booking booking = new Booking(null, userId, turfId, sportId, start, end, "CONFIRMED", LocalDateTime.now());
        long bookingId = bookingDao.create(booking);
        booking.setId(bookingId); // Set the generated ID on the object

        // --- EMAIL LOGIC ---
        try {
            User user = userDao.findById(userId); // Fetch user details for the email
            Booking detailedBooking = bookingDao.findById(bookingId); // Fetch booking with turf/sport names
            if (user != null && detailedBooking != null) {
                emailService.sendBookingConfirmation(user, detailedBooking);
            }
        } catch (Exception e) {
            System.err.println("Booking created, but failed to send confirmation email. Booking ID: " + bookingId);
            // Don't re-throw the exception, as the booking itself was successful.
        }

        return bookingId;
    }

    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and End time are required");
        if (!end.isAfter(start)) throw new IllegalArgumentException("End time must be after start time");
        if (start.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Start time cannot be in the past");
        Duration d = Duration.between(start, end);
        if (d.toMinutes() < 30) throw new IllegalArgumentException("Minimum booking duration is 30 minutes");
        if (d.toHours() > 4) throw new IllegalArgumentException("Maximum booking duration is 4 hours");
    }

    public List<Booking> getBookingsForUser(long userId) throws SQLException {
        List<Booking> bookings = bookingDao.findBookingsByUserId(userId);
        for (Booking booking : bookings) {
            boolean isCancellable = "CONFIRMED".equals(booking.getStatus()) &&
                    LocalDateTime.now().isBefore(booking.getStartTime().minusHours(CANCELLATION_CUTOFF_HOURS));
            booking.setCancellable(isCancellable);
        }
        return bookings;
    }

    public void cancelBooking(long bookingId, long userId) throws SQLException, SecurityException, IllegalStateException {
        // ... existing logic to find and validate booking ...
        Booking booking = bookingDao.findById(bookingId);
        if (booking == null) throw new IllegalArgumentException("Booking not found.");
        if (booking.getUserId() != userId) throw new SecurityException("You are not authorized to cancel this booking.");
        //... more validation ...

        bookingDao.updateStatus(bookingId, userId, "CANCELLED_BY_USER");

        // --- EMAIL LOGIC ---
        try {
            User user = userDao.findById(userId);
            if (user != null) {
                emailService.sendCancellationConfirmation(user, booking, "User");
            }
        } catch(Exception e) {
            System.err.println("Booking cancelled, but failed to send cancellation email. Booking ID: " + bookingId);
        }
    }

    public List<Booking> getFilteredBookings(Long turfId, Long sportId, String date) throws SQLException {
        return bookingDao.findFilteredBookings(turfId, sportId, date);
    }

    public void updateBookingStatusAsAdmin(long bookingId, String status) throws SQLException {
        bookingDao.adminUpdateStatus(bookingId, status);

        // --- EMAIL LOGIC ---
        try {
            Booking booking = bookingDao.findById(bookingId);
            User user = userDao.findById(booking.getUserId());
            if (user != null && "CANCELLED_BY_ADMIN".equals(status)) {
                emailService.sendCancellationConfirmation(user, booking, "Admin");
            }
            // Add more notifications here if needed (e.g., for re-confirmation)
        } catch (Exception e) {
            System.err.println("Admin updated booking status, but failed to send notification email. Booking ID: " + bookingId);
        }
    }
}
