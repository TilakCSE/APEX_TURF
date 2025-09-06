<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>APEX TURF - My Bookings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body>
<header class="navbar">
    <div class="brand">APEX TURF</div>
    <nav class="nav-links">
        <a href="${pageContext.request.contextPath}/booking" class="nav-link">Book a Slot</a>
    </nav>
</header>
<main class="container">
    <section class="card">
        <h1>My Bookings</h1>
        <p class="subtitle">Enter your email to find and manage your bookings.</p>

        <c:if test="${not empty successMessage}">
            <div class="alert success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert error">${errorMessage}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/my-bookings" class="form-grid">
            <input type="hidden" name="action" value="lookup">
            <label>
                <span>Enter Your Email</span>
                <input type="email" name="email" placeholder="you@example.com" value="${lookupEmail}" required/>
            </label>
            <button type="submit" class="btn-primary">Find My Bookings</button>
        </form>

        <%-- This section ensures the table is only rendered if bookings exist --%>
        <c:if test="${not empty bookings}">
            <table class="bookings-table">
                <thead>
                    <tr>
                        <th>Turf</th>
                        <th>Sport</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- The forEach loop creates a new table row <tr> for each booking --%>
                    <c:forEach items="${bookings}" var="b">
                        <tr>
                            <%-- Each piece of data is wrapped in its own table cell <td> --%>
                            <td>${b.turfName}</td>
                            <td>${b.sportName}</td>
                            <td>${b.formattedStartTime}</td>
                            <td>${b.formattedEndTime}</td>
                            <td>
                                <span class="status-${b.status.toLowerCase()}">${b.status}</span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${b.cancellable}">
                                        <form method="post" action="${pageContext.request.contextPath}/my-bookings" onsubmit="return confirm('Are you sure you want to cancel this booking?');">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="bookingId" value="${b.id}">
                                            <input type="hidden" name="email" value="${lookupEmail}">
                                            <button type="submit" class="btn-cancel">Cancel</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="action-muted">—</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </section>
</main>
</body>
</html>