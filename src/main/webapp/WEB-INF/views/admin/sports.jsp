<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <div class="admin-header">
        <h1>Manage Sports</h1>
        <a href="${pageContext.request.contextPath}/admin/sports?action=new" class="btn-primary">Add New Sport</a>
    </div>

    <div class="admin-card">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${sportList}" var="sport">
                    <tr>
                        <td>${sport.id}</td>
                        <td>${sport.name}</td>
                        <td>
                            <span class="status-${sport.active ? 'active' : 'inactive'}">
                                ${sport.active ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/sports?action=edit&id=${sport.id}" class="btn-edit">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/sports?action=delete&id=${sport.id}" class="btn-delete" onclick="return confirm('Are you sure? Deleting a sport may affect existing turf associations.')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>