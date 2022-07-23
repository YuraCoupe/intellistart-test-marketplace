<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
         <c:import url="${contextPath}/WEB-INF/jsp/header.jsp"/>
    </head>

    <body>
        <c:import url="${contextPath}/WEB-INF/jsp/navibar.jsp"/>
        <div class="container">
            <form action="/users/edit">
                <div class="form-group">
                    <label for="Id">User email:</label><br>
                    <select class="form-control" id="id" name="id">
                        <option disabled selected value> -- select email -- </option>
                        <c:forEach items="${users}" var="user">
                            <option value="${user.id}"><c:out value="${user.username}"/></option>
                        </c:forEach>
                    </select>
                </div>
                    <input type="submit" value="Search">
            </form><br>

            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group me-2" role="group" aria-label="Second group">
                   <a href="/users/new" type="button" class="btn btn-primary">New</a>
                </div>
                <div class="btn-group me-2" role="group" aria-label="Second group">
                   <a href="/users/" type="button" class="btn btn-success">All</a>
                   <a href="/users/administrators" type="button" class="btn btn-info">Admins</a>
                   <a href="/users/users" type="button" class="btn btn-info">Users</a>
                </div>
            </div>
            <div>
                <c:if test="${not empty errorMessage}">
                    <c:forEach items="${errorMessage.errors}" var="error">
                        <p style="color:red">${error}</p>
                    </c:forEach>
                </c:if>
            </div>

            <table class="table table-hover">
                <thead>
                    <tr>
                        <td>Id</td>
                        <td>Username</td>
                        <td>First name</td>
                        <td>Last name</td>
                        <td>Roles</td>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>
                            <c:out value="${user.id}"/>
                        </td>
                        <td>
                            <c:out value="${user.username}"/>
                        </td>
                        <td>
                            <c:out value="${user.firstName}"/><br>
                        </td>
                        <td>
                            <c:out value="${user.lastName}"/><br>
                        </td>
                        <td>
                            <c:forEach items="${user.roles}" var="role">
                                <c:out value="${role.name}"/><br>
                            </c:forEach>
                        </td>
                        <td>
                            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                     <a href="/users/edit/${user.id}" type="button" class="btn btn-warning">Edit</a>
                                     <a href="/users/delete/${user.id}" type="button" class="btn btn-danger">Remove</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
