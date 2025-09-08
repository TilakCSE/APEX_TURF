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
                    <%-- The ID here is used by the JavaScript --%>
                    <select name="turfId" id="turf-select" required>
                        <option value="" disabled selected>Select a turf</option>
                        <c:forEach items="${turfs}" var="t">
                            <%-- We add a data-sports attribute with the list of sport IDs --%>
                            <option value="${t.id}" data-sports="${t.sportIds}">${t.name} — ${t.location}</option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span>Sport</span>
                     <%-- The ID here is used by the JavaScript --%>
                    <select name="sportId" id="sport-select" required>
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

    <%-- ADD THIS SCRIPT BLOCK at the end of the body --%>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const turfSelect = document.getElementById('turf-select');
            const sportSelect = document.getElementById('sport-select');
            const sportOptions = Array.from(sportSelect.options);

            turfSelect.addEventListener('change', function () {
                // Get the list of allowed sport IDs from the selected turf's data-sports attribute
                const selectedTurfOption = turfSelect.options[turfSelect.selectedIndex];
                const allowedSports = selectedTurfOption.getAttribute('data-sports');

                // Reset sport selection
                sportSelect.value = '';

                // Filter the sport options
                sportOptions.forEach(option => {
                    // Always show the placeholder "Select a sport"
                    if (option.value === '') {
                        option.style.display = 'block';
                        return;
                    }

                    // Check if the sport's ID is in the list of allowed sports
                    if (allowedSports && allowedSports.includes(option.value)) {
                        option.style.display = 'block'; // Show the option
                    } else {
                        option.style.display = 'none'; // Hide the option
                    }
                });
            });

            // Initially, hide all sports except the placeholder until a turf is selected
            sportOptions.forEach(option => {
                if (option.value !== '') {
                    option.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html>