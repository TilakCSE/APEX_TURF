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

            <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>
            <c:if test="${not empty sessionScope.error}"><div class="alert error">${sessionScope.error}</div><c:remove var="error" scope="session"/></c:if>
            <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>

            <form method="post" action="${pageContext.request.contextPath}/booking" class="form-grid">

                <div class="turf-selection-group" style="grid-column: 1 / -1;">
                    <label>
                        <span>Turf</span>
                        <select name="turfId" id="turf-select" required>
                            <option value="" disabled selected>Select a turf to see details</option>
                            <c:forEach items="${turfs}" var="t">
                                <option value="${t.id}" data-sports="${t.sportIds}">${t.name} — ${t.location}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <a href="#" id="view-details-btn" class="btn-secondary disabled" target="_blank">View Details</a>
                </div>

                <label>
                    <span>Sport</span>
                    <select name="sportId" id="sport-select" required>
                        <option value="" disabled selected>Select a sport</option>
                        <c:forEach items="${sports}" var="s">
                            <option value="${s.id}">${s.name}</option>
                        </c:forEach>
                    </select>
                </label>

                <label><span>Start time</span><input type="datetime-local" name="startTime" required/></label>
                <label><span>End time</span><input type="datetime-local" name="endTime" required/></label>
                <button type="submit" class="btn-primary">Confirm Booking</button>
            </form>
        </section>
    </main>

    <%-- In booking.jsp, replace the entire <script>...</script> section --%>
    <%-- In booking.jsp, this is the final, correct script --%>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const turfSelect = document.getElementById('turf-select');
            const sportSelect = document.getElementById('sport-select');
            const sportOptions = Array.from(sportSelect.options);
            const detailsButton = document.getElementById('view-details-btn');

            turfSelect.addEventListener('change', function () {
                const selectedTurfId = this.value;

                if (selectedTurfId && selectedTurfId.trim() !== "") {
                    // CORRECTED LINE: The backslash tells the JSP server to ignore this variable.
                    const url = `turf-details?turfId=\${selectedTurfId}`;
                    detailsButton.href = url;
                    detailsButton.classList.remove('disabled');
                } else {
                    detailsButton.href = '#';
                    detailsButton.classList.add('disabled');
                }

                // --- Sport filtering logic (unchanged) ---
                const selectedTurfOption = turfSelect.options[turfSelect.selectedIndex];
                const allowedSports = selectedTurfOption.getAttribute('data-sports');
                sportSelect.value = '';
                sportOptions.forEach(option => {
                    if (option.value === '') { option.style.display = 'block'; return; }
                    if (allowedSports && allowedSports.includes(option.value)) {
                        option.style.display = 'block';
                    } else {
                        option.style.display = 'none';
                    }
                });
            });

            sportOptions.forEach(option => {
                if (option.value !== '') option.style.display = 'none';
            });
        });
    </script>
</body>
</html>