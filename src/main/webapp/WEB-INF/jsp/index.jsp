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
            Welcome to IntelliStart Test Marketplace.<br>
            Use navigation bar to work with database.
        </div>
    </body>
</html>
