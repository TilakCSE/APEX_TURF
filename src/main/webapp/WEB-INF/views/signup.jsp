<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign Up - APEX TURF</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>
    <main class="container">
        <section class="card">
            <h1>Create Account</h1>
            <p class="subtitle">Join APEX TURF to start booking.</p>

            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/signup" class="form-grid">
                <label>
                    <span>Full Name</span>
                    <input type="text" name="name" required placeholder="Your Name">
                </label>
                <label>
                    <span>Email</span>
                    <input type="email" name="email" required placeholder="you@example.com">
                </label>
                <label>
                    <span>Phone</span>
                    <input type="tel" name="phone" required placeholder="98765 43210">
                </label>
                <label>
                    <span>Password</span>
                    <input type="password" name="password" required placeholder="Create a strong password">
                </label>
                <button type="submit" class="btn-primary">Sign Up</button>
            </form>
        </section>
    </main>
</body>
</html>