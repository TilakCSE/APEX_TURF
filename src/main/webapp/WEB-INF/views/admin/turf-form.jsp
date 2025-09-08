<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <h1><c:out value="${turf.id == null ? 'Add New' : 'Edit'}"/> Turf</h1>

    <div class="admin-card">
        <form method="post" action="${pageContext.request.contextPath}/admin/turfs" class="admin-form">
            <c:if test="${turf.id != null}">
                <input type="hidden" name="id" value="${turf.id}">
            </c:if>

            <div class="form-group">
                <label for="name">Turf Name</label>
                <input type="text" id="name" name="name" value="${turf.name}" required>
            </div>

            <div class="form-group">
                <label for="location">Location</label>
                <input type="text" id="location" name="location" value="${turf.location}" required>
            </div>

            <div class="form-group">
                <label>Assignable Sports</label>
                <div class="checkbox-group">
                    <c:forEach items="${allSports}" var="sport">
                        <label class="checkbox-label">
                            <input type="checkbox" name="sportIds" value="${sport.id}"
                                <c:forEach items="${assignedSportIds}" var="assignedId">
                                    <c:if test="${sport.id == assignedId}">checked</c:if>
                                </c:forEach>
                            >
                            ${sport.name}
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label class="checkbox-label">
                    <input type="checkbox" name="active" ${turf.active ? 'checked' : ''}>
                    Active
                </label>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-primary">Save Turf</button>
                <a href="${pageContext.request.contextPath}/admin/turfs" class="btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>