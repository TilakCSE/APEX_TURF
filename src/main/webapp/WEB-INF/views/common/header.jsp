<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="navbar">
    <div class="brand">
        <a href="${pageContext.request.contextPath}/booking" style="color: inherit; text-decoration: none;">APEX TURF</a>
    </div>
    <nav class="nav-links">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <%-- User is Logged IN --%>
                <span class="nav-welcome">Welcome, ${sessionScope.user.name}</span>

                <a href="${pageContext.request.contextPath}/booking"
                   class="nav-link ${currentPage == 'booking' ? 'active' : ''}">Book a Slot</a>

                <a href="${pageContext.request.contextPath}/my-bookings"
                   class="nav-link ${currentPage == 'my-bookings' ? 'active' : ''}">My Bookings</a>

                <%-- THIS IS THE NEW ADMIN-ONLY LOGIC --%>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link nav-link-admin">Admin Panel</a>
                </c:if>

                <a href="${pageContext.request.contextPath}/logout" class="nav-link">Logout</a>
            </c:when>
            <c:otherwise>
                <%-- User is Logged OUT --%>
                <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
                <a href="${pageContext.request.contextPath}/signup" class="nav-link">Sign Up</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>