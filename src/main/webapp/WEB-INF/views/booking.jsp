<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book a Slot - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body data-context-path="${pageContext.request.contextPath}">
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="container">
        <section class="card">
            <h1>Book your turf</h1>
            <p class="subtitle">Select a turf, then pick an available time slot below.</p>

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

            <div class="slot-picker">
                <div class="day-selector" id="day-selector"></div>
                <div class="slot-container" id="slot-container">
                    <div class="loader"></div>
                </div>
            </div>
        </section>
    </main>

    <div id="booking-modal" class="modal-overlay">
        <div class="modal-content">
            <h2 id="modal-title">Book Slot</h2>
            <div id="modal-error" class="alert error" style="display:none;"></div>
            <form id="modal-booking-form">
                <input type="hidden" id="modal-turf-id" name="turfId">
                <input type="hidden" id="modal-sport-id" name="sportId" value="1"> <%-- Note: This is a placeholder. A sport selector could be added to the modal later. --%>
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
            const contextPath = document.body.dataset.contextPath;

            const turfSelect = document.getElementById('turf-select');
            const daySelector = document.getElementById('day-selector');
            const slotContainer = document.getElementById('slot-container');
            const detailsButton = document.getElementById('view-details-btn');
            const modal = document.getElementById('booking-modal');
            const modalForm = document.getElementById('modal-booking-form');
            const modalError = document.getElementById('modal-error');
            const closeModalBtn = document.getElementById('modal-close-btn');

            let selectedDate = new Date();

            // --- HELPER FUNCTIONS (Defined before they are used) ---

            function updateDetailsButton() {
                const turfId = turfSelect.value;
                if (turfId) {
                    detailsButton.href = `${contextPath}/turf-details?turfId=${turfId}`;
                    detailsButton.classList.remove('disabled');
                } else {
                    detailsButton.href = '#';
                    detailsButton.classList.add('disabled');
                }
            }

            function toLocalISOString(date) {
                const tzoffset = (new Date()).getTimezoneOffset() * 60000;
                return (new Date(date - tzoffset)).toISOString().slice(0, 16);
            }

            function renderDaySelector() {
                daySelector.innerHTML = '';
                for (let i = 0; i < 7; i++) {
                    const date = new Date();
                    date.setDate(date.getDate() + i);
                    const button = document.createElement('button');
                    button.className = 'day-btn';
                    button.innerHTML = `<span>${['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'][date.getDay()]}</span>${date.getDate()}`;
                    if (i === 0) button.classList.add('active');

                    button.addEventListener('click', () => {
                        document.querySelector('.day-btn.active').classList.remove('active');
                        button.classList.add('active');
                        selectedDate = date;
                        loadSlotsForDate(date);
                    });
                    daySelector.appendChild(button);
                }
            }

            async function loadSlotsForDate(date) {
                slotContainer.innerHTML = '<div class="loader"></div>';
                const turfId = turfSelect.value;
                if (!turfId) {
                    slotContainer.innerHTML = '<div class="slot-error">Please select a turf first.</div>';
                    return;
                }
                const startOfDay = new Date(date.setHours(0, 0, 0, 0)).toISOString();
                const endOfDay = new Date(date.setHours(23, 59, 59, 999)).toISOString();

                try {
                    const apiUrl = `${contextPath}/api/bookings?turfId=${turfId}&start=${startOfDay}&end=${endOfDay}`;
                    const response = await fetch(apiUrl);
                    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                    const bookedSlots = await response.json();
                    renderSlots(date, bookedSlots);
                } catch (error) {
                    slotContainer.innerHTML = '<div class="slot-error">Could not load slots.</div>';
                    console.error('Error fetching slots:', error);
                }
            }

            function renderSlots(date, bookedSlots) {
                slotContainer.innerHTML = '';
                for (let hour = 6; hour < 24; hour++) {
                    for (let minute = 0; minute < 60; minute += 60) {
                        const slotTime = new Date(date);
                        slotTime.setHours(hour, minute, 0, 0);
                        const slot = document.createElement('button');
                        slot.className = 'slot';
                        const timeString = slotTime.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
                        slot.textContent = timeString;

                        const isBooked = bookedSlots.some(booked => {
                            const start = new Date(booked.start);
                            const end = new Date(booked.end);
                            return slotTime >= start && slotTime < end;
                        });

                        if (isBooked || slotTime < new Date()) {
                            slot.classList.add('booked');
                            slot.disabled = true;
                        } else {
                            slot.classList.add('available');
                            slot.addEventListener('click', () => handleSlotClick(slotTime));
                        }
                        slotContainer.appendChild(slot);
                    }
                }
            }

            function handleSlotClick(startTime) {
                const endTime = new Date(startTime.getTime() + 60 * 60 * 1000);
                document.getElementById('modal-turf-id').value = turfSelect.value;
                document.getElementById('modal-start-time').value = toLocalISOString(startTime);
                document.getElementById('modal-end-time').value = toLocalISOString(endTime);
                document.getElementById('modal-title').innerText = `Book Slot at ${turfSelect.options[turfSelect.selectedIndex].text}`;
                modalError.style.display = 'none';
                modal.style.display = 'flex';
            }

            async function handleFormSubmit(e) {
                e.preventDefault();
                const response = await fetch(`${contextPath}/booking`, {
                    method: 'POST',
                    body: new URLSearchParams(new FormData(modalForm))
                });
                const data = await response.json();
                if (data.success) {
                    modal.style.display = 'none';
                    loadSlotsForDate(selectedDate); // Refresh slots
                } else {
                    modalError.textContent = data.message;
                    modalError.style.display = 'block';
                }
            }

            // --- EVENT LISTENERS & INITIALIZER ---

            function initialize() {
                renderDaySelector();
                updateDetailsButton();
                loadSlotsForDate(selectedDate);

                turfSelect.addEventListener('change', () => {
                    updateDetailsButton();
                    loadSlotsForDate(selectedDate);
});
                modalForm.addEventListener('submit', handleFormSubmit);
                closeModalBtn.addEventListener('click', () => modal.style.display = 'none');
                modal.addEventListener('click', (e) => {
                    if (e.target === modal) modal.style.display = 'none';
                });
            }

            initialize();
        });
    </script>
</body>
</html>