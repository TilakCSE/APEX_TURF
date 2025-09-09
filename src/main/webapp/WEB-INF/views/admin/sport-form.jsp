<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <h1><c:out value="${sport.id == null ? 'Add New' : 'Edit'}"/> Sport</h1>

    <div class="admin-card">
        <form method="post" action="${pageContext.request.contextPath}/admin/sports" class="admin-form">
            <c:if test="${sport.id != null}">
                <input type="hidden" name="id" value="${sport.id}">
            </c:if>

            <div class="form-group">
                <label for="name">Sport Name</label>
                <input type="text" id="name" name="name" value="${sport.name}" required>
            </div>

            <div class="form-group">
                <label class="checkbox-label">
                    <input type="checkbox" name="active" ${sport.active ? 'checked' : ''}>
                    Active
                </label>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-primary">Save Sport</button>
                <a href="${pageContext.request.contextPath}/admin/sports" class="btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>