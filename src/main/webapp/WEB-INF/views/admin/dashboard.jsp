<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <h1>Welcome, ${sessionScope.user.name}!</h1>
    <div class="admin-card">
        <p>This is the admin dashboard. From here you will be able to manage turfs, sports, and all user bookings.</p>
        <p>Select an option from the sidebar to get started.</p>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>