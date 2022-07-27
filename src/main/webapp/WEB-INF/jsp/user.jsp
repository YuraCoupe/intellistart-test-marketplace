<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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
            <div class="row">
                <security:authorize access="hasRole('ROLE_ADMIN')">
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <a href="/users" type="button" class="btn btn-success">Back to users</a>
                    </div>
                </div>
                </security:authorize>
            </div><br>
        <form:form action="/users/save" method="post" modelAttribute="user">
            <div class="form-group">
                <div class="row">
                    <form:label path="id">User ID:</form:label><br>
                    <form:input path="id" type="UUID" readonly="true" class="form-control" id="id" placeholder="User ID" name="id" value="${user.id}"/>
                    <br>
                    <form:label path="username">username (email address):</form:label><br>
                    <form:input path="username" type="text" class="form-control" id="username" placeholder="Enter username" name="username" value="${user.username}"/>
                    <form:errors path="username" cssClass="error"/>
                    <br>
                    <form:label path="password">Password:</form:label><br>
                    <form:input path="password" type="text" class="form-control" id="password" placeholder="Enter password" name="password" value="${user.password}"/>
                    <form:errors path="password" cssClass="error"/>
                    <br>
                    <form:label path="firstName">First name:</form:label><br>
                    <form:input path="firstName" type="text" class="form-control" id="firstName" placeholder="Enter first name" name="firstName" value="${user.firstName}"/>
                    <form:errors path="firstName" cssClass="error"/>
                    <br>
                    <form:label path="lastName">First name:</form:label><br>
                    <form:input path="lastName" type="text" class="form-control" id="lastName" placeholder="Enter last name" name="lastName" value="${user.lastName}"/>
                    <form:errors path="lastName" cssClass="error"/>
                    <br>
                    <form:label path="money">Money:</form:label><br>
                    <form:input path="money" type="number" step="0.01" class="form-control" id="money" placeholder="Enter money" name="money" value="${user.money}"/>
                    <form:errors path="money" cssClass="error"/>
                    <br>
                    <security:authorize access="hasRole('ROLE_ADMIN')">
                    <form:label path="roles">Roles:</form:label><br>
                    <c:forEach items="${roles}" var="role">
                        <form:checkbox path="roles" id="${roles}" label="${role.name}" value="${role}"/></td>
                    </c:forEach>
                    <br>
                    <form:errors path="roles" cssClass="error"/><br><br>
                    </security:authorize>
                </div>
                <div class="row">
                    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                        <div class="btn-group me-2" role="group" aria-label="Second group">
                            <form:button type="submit" value="Submit" class="btn btn-primary">Save</form:button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
        </div>
    </body>
</html>
