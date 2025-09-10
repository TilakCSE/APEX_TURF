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

            <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
            <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

            <form method="post" action="${pageContext.request.contextPath}/booking" class="form-grid">

                <%-- UPDATED: Turf selection is now wrapped in a div with the button --%>
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
                    <%-- Ensure the class="btn-secondary disabled" is on the <a> tag --%>
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

                <%-- The rest of the form is unchanged --%>
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

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const turfSelect = document.getElementById('turf-select');
            const sportSelect = document.getElementById('sport-select');
            const sportOptions = Array.from(sportSelect.options);
            const detailsButton = document.getElementById('view-details-btn'); // Get the button

            turfSelect.addEventListener('change', function () {
                const selectedTurfOption = turfSelect.options[turfSelect.selectedIndex];
                const allowedSports = selectedTurfOption.getAttribute('data-sports');
                const selectedTurfId = this.value;

                // --- Logic for the "View Details" button ---
                if (selectedTurfId) {
                    detailsButton.href = `turf-details?turfId=${selectedTurfId}`;
                    detailsButton.classList.remove('disabled');
                } else {
                    detailsButton.href = '#';
                    detailsButton.classList.add('disabled');
                }

                // --- Logic for filtering sports (unchanged) ---
                sportSelect.value = '';
                sportOptions.forEach(option => {
                    if (option.value === '') {
                        option.style.display = 'block';
                        return;
                    }
                    if (allowedSports && allowedSports.includes(option.value)) {
                        option.style.display = 'block';
                    } else {
                        option.style.display = 'none';
                    }
                });
            });

            // Initially, hide all sports except the placeholder
            sportOptions.forEach(option => {
                if (option.value !== '') option.style.display = 'none';
            });
        });
    </script>
</body>
</html>