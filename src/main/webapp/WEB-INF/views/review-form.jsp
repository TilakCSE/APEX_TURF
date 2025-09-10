<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Leave a Review - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <style>
        .star-rating { display: flex; flex-direction: row-reverse; justify-content: flex-end; }
        .star-rating input { display: none; }
        .star-rating label { font-size: 2rem; color: #ddd; cursor: pointer; transition: color 0.2s; }
        .star-rating input:checked ~ label, .star-rating label:hover, .star-rating label:hover ~ label { color: #f59e0b; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="container">
    <section class="card">
        <h1>Leave a Review</h1>
        <p class="subtitle">Share your experience for your booking at <strong>${booking.turfName}</strong> on <strong>${booking.formattedStartTime}</strong>.</p>

        <form method="post" action="${pageContext.request.contextPath}/review" class="form-grid">
            <input type="hidden" name="bookingId" value="${booking.id}">
            <input type="hidden" name="turfId" value="${booking.turfId}">

            <div style="grid-column: 1 / -1;">
                <label><span>Your Rating</span></label>
                <div class="star-rating">
                    <input type="radio" id="star5" name="rating" value="5" required/><label for="star5" title="5 stars">&#9733;</label>
                    <input type="radio" id="star4" name="rating" value="4"/><label for="star4" title="4 stars">&#9733;</label>
                    <input type="radio" id="star3" name="rating" value="3"/><label for="star3" title="3 stars">&#9733;</label>
                    <input type="radio" id="star2" name="rating" value="2"/><label for="star2" title="2 stars">&#9733;</label>
                    <input type="radio" id="star1" name="rating" value="1"/><label for="star1" title="1 star">&#9733;</label>
                </div>
            </div>

            <label style="grid-column: 1 / -1;">
                <span>Your Comments (Optional)</span>
                <textarea name="comment" rows="5" placeholder="How was your experience?"></textarea>
            </label>

            <button type="submit" class="btn-primary">Submit Review</button>
        </form>
    </section>
</main>
</body>
</html>