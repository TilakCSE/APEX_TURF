<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book a Slot - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.13/index.global.min.js'></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="container">
        <section class="card">
            <h1>Book your turf</h1>
            <p class="subtitle">Select a turf to see its schedule, then click an open time slot to book.</p>

            <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
            <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>
            <c:if test="${not empty sessionScope.error}"><div class="alert error">${sessionScope.error}</div><c:remove var="error" scope="session"/></c:if>

            <div class="turf-selection-group" style="margin-bottom: 2rem;">
                <label style="flex-grow: 1;">
                    <span>Turf</span>
                    <select name="turfId" id="turf-select" required>
                        <c:forEach items="${turfs}" var="t">
                            <option value="${t.id}" ${t.id == initialTurfId ? 'selected' : ''}>${t.name} — ${t.location}</option>
                        </c:forEach>
                    </select>
                </label>
                <a href="#" id="view-details-btn" class="btn-secondary" target="_blank">View Details</a>
            </div>

            <div id="booking-calendar"></div>
        </section>
    </main>

    <div id="booking-modal" class="modal-overlay">
        <div class="modal-content">
            <h2 id="modal-title">Book Slot</h2>
            <div id="modal-error" class="alert error" style="display:none;"></div>
            <form id="modal-booking-form">
                <input type="hidden" id="modal-turf-id" name="turfId">
                <input type="hidden" id="modal-sport-id" name="sportId" value="1"> <%-- Defaulting to sport 1, can be improved --%>
                <label><span>Start time</span><input type="datetime-local" id="modal-start-time" name="startTime" required/></label>
                <label><span>End time</span><input type="datetime-local" id="modal-end-time" name="endTime" required/></label>
                <div class="modal-actions">
                    <button type="button" id="modal-close-btn" class="btn-secondary">Close</button>
                    <button type="submit" class="btn-primary">Confirm Booking</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const turfSelect = document.getElementById('turf-select');
            const detailsButton = document.getElementById('view-details-btn');
            const calendarEl = document.getElementById('booking-calendar');

            // --- Calendar Initialization ---
            const calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'timeGridWeek',
                headerToolbar: { left: 'prev,next today', center: 'title', right: 'timeGridWeek,timeGridDay' },
                slotMinTime: '06:00:00',
                slotMaxTime: '24:00:00',
                allDaySlot: false,
                events: function(fetchInfo, successCallback, failureCallback) {
                    const turfId = turfSelect.value;
                    fetch(`${pageContext.request.contextPath}/api/bookings?turfId=\${turfId}&start=\${fetchInfo.startStr}&end=\${fetchInfo.endStr}`)
                        .then(response => response.json())
                        .then(data => successCallback(data))
                        .catch(error => failureCallback(error));
                },
                eventColor: '#dc2626',
                dateClick: handleDateClick
            });
            calendar.render();

            // --- Event Handlers ---
            turfSelect.addEventListener('change', function() {
                updateDetailsButton();
                calendar.refetchEvents(); // Reload events for the new turf
            });

            function updateDetailsButton() {
                const turfId = turfSelect.value;
                if (turfId) {
                    detailsButton.href = `turf-details?turfId=\${turfId}`;
                    detailsButton.classList.remove('disabled');
                }
            }

            // --- Modal Logic ---
            const modal = document.getElementById('booking-modal');
            const modalForm = document.getElementById('modal-booking-form');
            const modalError = document.getElementById('modal-error');
            const closeModalBtn = document.getElementById('modal-close-btn');

            function handleDateClick(info) {
                if (info.date < new Date()) {
                    alert("You cannot book a time slot in the past.");
                    return;
                }
                const start = info.date;
                const end = new Date(start.getTime() + 60 * 60 * 1000); // Default 1 hour booking

                document.getElementById('modal-turf-id').value = turfSelect.value;
                document.getElementById('modal-start-time').value = toLocalISOString(start);
                document.getElementById('modal-end-time').value = toLocalISOString(end);

                document.getElementById('modal-title').innerText = `Book Slot at \${turfSelect.options[turfSelect.selectedIndex].text}`;
                modalError.style.display = 'none';
                modal.style.display = 'flex';
            }

            closeModalBtn.addEventListener('click', () => modal.style.display = 'none');
            modal.addEventListener('click', (e) => {
                if (e.target === modal) modal.style.display = 'none';
            });

            modalForm.addEventListener('submit', function(e) {
                e.preventDefault();
                fetch('${pageContext.request.contextPath}/booking', {
                    method: 'POST',
                    body: new URLSearchParams(new FormData(modalForm))
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        modal.style.display = 'none';
                        calendar.refetchEvents();
                    } else {
                        modalError.textContent = data.message;
                        modalError.style.display = 'block';
                    }
                });
            });

            // --- Initial Setup ---
            updateDetailsButton(); // Set the initial details button link
        });

        function toLocalISOString(date) {
            const tzoffset = (new Date()).getTimezoneOffset() * 60000;
            return (new Date(date - tzoffset)).toISOString().slice(0, 16);
        }
    </script>
</body>
</html>