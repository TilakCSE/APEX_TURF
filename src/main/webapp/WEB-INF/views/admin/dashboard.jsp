<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <div class="admin-header">
        <h1>Bookings Dashboard</h1>
    </div>

    <%-- Display flash messages from session --%>
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert success">${sessionScope.successMessage}</div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert error">${sessionScope.errorMessage}</div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="admin-card">
        <form action="${pageContext.request.contextPath}/admin/dashboard" method="get" class="filter-form">
            <select name="turfFilter">
                <option value="">All Turfs</option>
                <c:forEach items="${allTurfs}" var="t">
                    <option value="${t.id}" ${selectedTurf == t.id ? 'selected' : ''}>${t.name}</option>
                </c:forEach>
            </select>
            <select name="sportFilter">
                <option value="">All Sports</option>
                <c:forEach items="${allSports}" var="s">
                    <option value="${s.id}" ${selectedSport == s.id ? 'selected' : ''}>${s.name}</option>
                </c:forEach>
            </select>
            <input type="date" name="dateFilter" value="${selectedDate}">
            <button type="submit" class="btn-primary">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-secondary">Clear</a>
        </form>
    </div>

    <div class="admin-card">
        <div class="export-link">
            <a href="${pageContext.request.contextPath}/admin/bookings/export?turfFilter=${selectedTurf}&sportFilter=${selectedSport}&dateFilter=${selectedDate}">Export as CSV</a>
        </div>
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Turf</th>
                    <th>Sport</th>
                    <th>Time Slot</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${allBookings}" var="b">
                    <tr>
                        <td>${b.id}</td>
                        <td>${b.userName}</td>
                        <td>${b.turfName}</td>
                        <td>${b.sportName}</td>
                        <td>${b.formattedStartTime}</td>
                        <td>
                            <span class="status-${b.status.toLowerCase().contains('cancelled') ? 'cancelled' : b.status.toLowerCase()}">
                                    ${b.status.replace('_', ' ')}
                            </span>
                        </td>
                        <td class="action-buttons">
                            <%-- The "Confirm" button will now ONLY show if the status is CANCELLED_BY_ADMIN --%>
                            <c:if test="${b.status == 'CANCELLED_BY_ADMIN'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin/dashboard">
                                    <input type="hidden" name="action" value="confirm">
                                    <input type="hidden" name="bookingId" value="${b.id}">
                                    <button type="submit" class="btn-confirm">Confirm</button>
                                </form>
                            </c:if>
                            <c:if test="${not b.status.startsWith('CANCELLED')}">
                                     <form method="post" action="${pageContext.request.contextPath}/admin/dashboard">
                                         <input type="hidden" name="action" value="cancel">
                                         <input type="hidden" name="bookingId" value="${b.id}">
                                         <button type="submit" class="btn-delete">Cancel</button>
                                     </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>