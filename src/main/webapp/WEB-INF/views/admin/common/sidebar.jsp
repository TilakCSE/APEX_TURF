<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="admin-sidebar">
    <div class="admin-sidebar-header">
        APEX TURF Admin
    </div>
    <nav class="admin-sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="${pageTitle == 'Dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="#" class="${pageTitle == 'Turfs' ? 'active' : ''}">Manage Turfs</a>
        <a href="#" class="${pageTitle == 'Sports' ? 'active' : ''}">Manage Sports</a>
    </nav>
    <div class="admin-sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</aside>