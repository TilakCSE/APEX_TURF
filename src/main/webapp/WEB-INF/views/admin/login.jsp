<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-styles.css"/>
</head>
<body class="admin">
    <div class="admin-login-wrapper">
        <div class="card admin-login-card">
            <h1>Admin Login</h1>
            <c:if test="${not empty error}">
                <div class="alert error">${error}</div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/admin/login">
                <label>
                    <span>Email</span>
                    <input type="email" name="email" required>
                </label>
                <br>
                <label>
                    <span>Password</span>
                    <input type="password" name="password" required>
                </label>
                <br>
                <button type="submit" class="btn-primary" style="width:100%;">Login</button>
            </form>
        </div>
    </div>
</body>
</html>