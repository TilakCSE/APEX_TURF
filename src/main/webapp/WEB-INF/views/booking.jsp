<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>APEX TURF - Book a Slot</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body>
<header class="navbar">
    <div class="brand">APEX TURF</div>
</header>
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
                <span>Name</span>
                <input type="text" name="name" placeholder="Your full name" required/>
            </label>

            <label>
                <span>Email</span>
                <input type="email" name="email" placeholder="you@example.com" required/>
            </label>

            <label>
                <span>Phone</span>
                <input type="tel" name="phone" placeholder="98765 43210" required/>
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
        <p class="help-text">Inspired by hudle.in — this is a minimal MVP for quick validation.</p>
    </section>
</main>
</body>
</html>
