<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="container">
        <section class="card">
            <h1>Login</h1>
            <p class="subtitle">Access your account to manage your bookings.</p>

            <c:if test="${param.success == 'true'}">
                <div class="alert success">Registration successful! Please log in.</div>
            </c:if>
            <c:if test="${param.logout == 'true'}">
                <div class="alert success">You have been logged out successfully.</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login" class="form-grid">
                <label style="grid-column: 1 / -1;">
                    <span>Email</span>
                    <input type="email" name="email" required placeholder="you@example.com">
                </label>
                <label style="grid-column: 1 / -1;">
                    <span>Password</span>
                    <input type="password" name="password" required placeholder="••••••••">
                </label>
                <button type="submit" class="btn-primary">Login</button>
            </form>
        </section>
    </main>
</body>
</html>