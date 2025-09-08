<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/sidebar.jsp"/>

<main class="admin-main">
    <div class="admin-header">
        <h1>Manage Turfs</h1>
        <a href="${pageContext.request.contextPath}/admin/turfs?action=new" class="btn-primary">Add New Turf</a>
    </div>

    <div class="admin-card">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Location</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${turfList}" var="turf">
                    <tr>
                        <td>${turf.id}</td>
                        <td>${turf.name}</td>
                        <td>${turf.location}</td>
                        <td>
                            <span class="status-${turf.active ? 'active' : 'inactive'}">
                                ${turf.active ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/turfs?action=edit&id=${turf.id}" class="btn-edit">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/turfs?action=delete&id=${turf.id}" class="btn-delete" onclick="return confirm('Are you sure?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>