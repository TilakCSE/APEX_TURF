<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
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

        <h3>Availability Timetable</h3>
        <div id="booking-calendar"></div>

        <div class="avg-rating-box">
            <div class="avg-rating-value"><fmt:formatNumber value="${averageRating}" maxFractionDigits="1"/></div>
            <div class="review-stars">
                <c:forEach begin="1" end="${Math.round(averageRating)}">&#9733;</c:forEach><c:forEach begin="${Math.round(averageRating) + 1}" end="5">&#9734;</c:forEach>
            </div>
            <p class="subtitle">Average Rating</p>
        </div>
        <h2>Recent Reviews</h2>
        <c:choose>
            <c:when test="${not empty reviews}">
                <c:forEach items="${reviews}" var="review"><div class="review-card">...</div></c:forEach>
            </c:when>
            <c:otherwise><p class="subtitle">No reviews yet.</p></c:otherwise>
        </c:choose>
    </section>
</main>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const calendarEl = document.getElementById('booking-calendar');
        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'timeGridWeek',
            headerToolbar: { left: 'prev,next', center: 'title', right: '' },
            slotMinTime: '06:00:00',
            slotMaxTime: '24:00:00',
            allDaySlot: false,
            events: '${pageContext.request.contextPath}/api/bookings?turfId=${turf.id}',
            eventColor: '#34495e',
            // These options make it read-only
            selectable: false,
            editable: false,
            dateClick: null,
            eventClick: null
        });
        calendar.render();
    });
</script>
</body>
</html>