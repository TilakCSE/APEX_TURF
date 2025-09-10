<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <%-- This line is important for number formatting --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${turf.name} - Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <style>
        .review-card { border: 1px solid var(--border-color); border-radius: 8px; padding: 16px; margin-bottom: 16px; }
        .review-header { display: flex; justify-content: space-between; align-items: center; }
        .review-user { font-weight: bold; }
        .review-stars { color: #f59e0b; }
        .review-comment { margin-top: 8px; color: var(--muted); font-style: italic; }
        .avg-rating-box { text-align: center; margin-bottom: 2rem; }
        .avg-rating-value { font-size: 3rem; font-weight: bold; color: #f59e0b; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="container">
    <section class="card">
        <h1>${turf.name}</h1>
        <p class="subtitle">${turf.location}</p>

        <div class="avg-rating-box">
            <div class="avg-rating-value">
                <fmt:formatNumber value="${averageRating}" maxFractionDigits="1"/>
            </div>
            <div class="review-stars">
                <c:forEach begin="1" end="${Math.round(averageRating)}">&#9733;</c:forEach>
                <c:forEach begin="${Math.round(averageRating) + 1}" end="5">&#9734;</c:forEach>
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
                            <span class="review-stars">
                                <c:forEach begin="1" end="${review.rating}">&#9733;</c:forEach>
                                <c:forEach begin="${review.rating + 1}" end="5">&#9734;</c:forEach>
                            </span>
                        </div>
                        <p class="review-comment">"${review.comment}"</p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="subtitle">No reviews yet. Be the first to leave one!</p>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}/booking" class="btn-primary" style="text-align:center; display:block; margin-top:2rem;">Book a Turf</a>
    </section>
</main>
</body>
</html>