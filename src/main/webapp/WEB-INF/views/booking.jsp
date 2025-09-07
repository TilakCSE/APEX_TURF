<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book a Slot - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="container">
        <section class="card">
            <h1>Book your turf</h1>
            <p class="subtitle">Select turf, sport, and a convenient time slot.</p>

            <c:if test="${not empty success}">
                <div class="alert success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/booking" class="form-grid">
                <label>
                    <span>Turf</span>
                    <select name="turfId" required>
                        <option value="" disabled selected>Select a turf</option>
                        <c:forEach items="${turfs}" var="t">
                            <option value="${t.id}">${t.name} — ${t.location}</option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span>Sport</span>
                    <select name="sportId" required>
                        <option value="" disabled selected>Select a sport</option>
                        <c:forEach items="${sports}" var="s">
                            <option value="${s.id}">${s.name}</option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span>Start time</span>
                    <input type="datetime-local" name="startTime" required/>
                </label>
                <label>
                    <span>End time</span>
                    <input type="datetime-local" name="endTime" required/>
                </label>
                <button type="submit" class="btn-primary">Confirm Booking</button>
            </form>
        </section>
    </main>
</body>
</html>