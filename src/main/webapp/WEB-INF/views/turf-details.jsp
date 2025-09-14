<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${turf.name} - Book a Slot</title>
    <title>${turf.name} - Details & Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.13/index.global.min.js'></script>
    <style>
        .review-card { border: 1px solid var(--border-color); border-radius: 8px; padding: 16px; margin-bottom: 16px; }
        .review-header { display: flex; justify-content: space-between; align-items: center; }
        .review-user { font-weight: bold; }
        .review-stars { color: #f59e0b; }
        .review-comment { margin-top: 8px; color: var(--muted); font-style: italic; }
        .avg-rating-box { text-align: center; margin: 1rem 0 2rem 0; }
        .avg-rating-value { font-size: 3rem; font-weight: bold; color: #f59e0b; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="container">
    <section class="card">
        <h1>${turf.name}</h1>
        <p class="subtitle">${turf.location}</p>
        <a href="${pageContext.request.contextPath}/booking" class="btn-primary" style="text-align:center; display:block; margin:2rem 0;">Book Now</a>

        <%-- Visual Booking Calendar --%>
        <h3>Availability Timetable</h3>
        <div id="booking-calendar"></div>

        <%-- Average Rating & Reviews --%>
        <div class="avg-rating-box">
            <div class="avg-rating-value"><fmt:formatNumber value="${averageRating}" maxFractionDigits="1"/></div>
            <div class="review-stars">
                 <c:forEach begin="1" end="${Math.round(averageRating)}">&#9733;</c:forEach><c:forEach begin="${Math.round(averageRating) + 1}" end="5">&#9734;</c:forEach>
                <c:forEach begin="1" end="${Math.round(averageRating)}">&#9733;</c:forEach><c:forEach begin="${Math.round(averageRating) + 1}" end="5">&#9734;</c:forEach>
            </div>
            <p class="subtitle">Average Rating</p>
        </div>

        <h2>Recent Reviews</h2>
        <c:choose>
            <c:when test="${not empty reviews}">
                <c:forEach items="${reviews}" var="review">
                    <div class="review-card">
                        <div class="review-header">
                            <span class="review-user">${review.userName}</span>
                            <span class="review-stars"><c:forEach begin="1" end="${review.rating}">&#9733;</c:forEach><c:forEach begin="${review.rating + 1}" end="5">&#9734;</c:forEach></span>
                        </div>
                        <p class="review-comment">"${review.comment}"</p>
                    </div>
                </c:forEach>
                <c:forEach items="${reviews}" var="review"><div class="review-card">...</div></c:forEach>
            </c:when>
            <c:otherwise><p class="subtitle">No reviews yet.</p></c:otherwise>
        </c:choose>
    </section>
</main>

<%-- Booking Modal (Initially Hidden) --%>
<div id="booking-modal" class="modal-overlay">
    <div class="modal-content">
        <h2>Book Slot at ${turf.name}</h2>
        <p class="subtitle">Confirm your start and end times.</p>
        <div id="modal-error" class="alert error" style="display:none;"></div>
        <form id="modal-booking-form">
            <input type="hidden" name="turfId" value="${turf.id}">
            <input type="hidden" name="sportId" value="1"> <%-- Placeholder sportId, can be made dynamic --%>
            <label><span>Start time</span><input type="datetime-local" id="modal-start-time" name="startTime" required/></label>
            <label><span>End time</span><input type="datetime-local" id="modal-end-time" name="endTime" required/></label>
            <div class="modal-actions">
                <button type="button" id="modal-close-btn" class="btn-secondary">Close</button>
                <button type="submit" class="btn-primary">Confirm Booking</button>
            </div>
        </form>
    </div>
</div>

<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.13/index.global.min.js'></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const calendarEl = document.getElementById('booking-calendar');
        const modal = document.getElementById('booking-modal');
        const modalForm = document.getElementById('modal-booking-form');
        const modalError = document.getElementById('modal-error');
        const closeModalBtn = document.getElementById('modal-close-btn');

        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'timeGridWeek',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'timeGridWeek,timeGridDay'
            },
            slotMinTime: '06:00:00', // Business Rule: No bookings before 6 AM
            slotMaxTime: '24:00:00', // 24:00 represents end of day
            headerToolbar: { left: 'prev,next', center: 'title', right: '' },
            slotMinTime: '06:00:00',
            slotMaxTime: '24:00:00',
            allDaySlot: false,
            events: '${pageContext.request.contextPath}/api/bookings?turfId=${turf.id}',
            eventColor: '#dc2626', // Color for booked slots

            dateClick: function(info) {
                if (info.date < new Date()) {
                    alert("You cannot book a time slot in the past.");
                    return;
                }

                const start = info.date;
                const end = new Date(start.getTime() + 60 * 60 * 1000); // Default 1 hour booking

                // Format for datetime-local input
                document.getElementById('modal-start-time').value = toLocalISOString(start);
                document.getElementById('modal-end-time').value = toLocalISOString(end);

                modalError.style.display = 'none';
                modal.style.display = 'flex';
            }
            eventColor: '#34495e',
            // These options make it read-only
            selectable: false,
            editable: false,
            dateClick: null,
            eventClick: null
        });
        calendar.render();

        // --- Modal Logic ---
        closeModalBtn.addEventListener('click', () => modal.style.display = 'none');
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });

        modalForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(modalForm);

            fetch('${pageContext.request.contextPath}/booking', {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    modal.style.display = 'none';
                    calendar.refetchEvents(); // Refresh the calendar to show new booking
                } else {
                    modalError.textContent = data.message;
                    modalError.style.display = 'block';
                }
            })
            .catch(error => {
                modalError.textContent = 'An unexpected error occurred. Please try again.';
                modalError.style.display = 'block';
            });
        });
    });

    function toLocalISOString(date) {
        const tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
        const localISOTime = (new Date(date - tzoffset)).toISOString().slice(0, 16);
        return localISOTime;
    }
</script>
</body>
</html>