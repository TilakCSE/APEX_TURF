<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <div class="admin-header">
        <h1>Manage User Reviews</h1>
    </div>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert success">${sessionScope.successMessage}</div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert error">${sessionScope.errorMessage}</div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="admin-card">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Turf</th>
                    <th>Rating</th>
                    <th>Comment</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${reviewList}" var="review">
                    <tr>
                        <td>${review.id}</td>
                        <td>${review.userName}</td>
                        <td>${review.turfName}</td>
                        <td><span class="review-stars">${review.rating} &#9733;</span></td>
                        <td><c:out value="${review.comment}" /></td>
                        <td>${review.createdAt.toLocalDate()}</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/admin/reviews" onsubmit="return confirm('Are you sure you want to delete this review permanently?');">
                                <input type="hidden" name="reviewId" value="${review.id}">
                                <button type="submit" class="btn-delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>