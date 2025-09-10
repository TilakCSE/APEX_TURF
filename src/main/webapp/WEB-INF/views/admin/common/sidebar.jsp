<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="admin-sidebar">
    <div class="admin-sidebar-header">
        APEX TURF Admin
    </div>
    <nav class="admin-sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="${pageTitle == 'Dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/analytics" class="${pageTitle == 'Analytics' ? 'active' : ''}">Analytics</a>
        <a href="${pageContext.request.contextPath}/admin/turfs" class="${pageTitle == 'Turfs' ? 'active' : ''}">Manage Turfs</a>
        <a href="${pageContext.request.contextPath}/admin/sports" class="${pageTitle == 'Sports' ? 'active' : ''}">Manage Sports</a>
        <a href="${pageContext.request.contextPath}/admin/reviews" class="${pageTitle == 'Reviews' ? 'active' : ''}">Manage Reviews</a>
    </nav>
    <div class="admin-sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</aside>