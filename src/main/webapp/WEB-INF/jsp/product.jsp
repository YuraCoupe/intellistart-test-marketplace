<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <a href="/products" type="button" class="btn btn-success">Back to products</a>
                    </div>
                </div>
            </div><br>
        <form:form action="/products" method="post" modelAttribute="product">
            <div class="form-group">
                <div class="row">
                    <form:label path="id">Product ID:</form:label><br>
                    <form:input path="id" type="UUID" readonly="true" class="form-control" id="id" placeholder="Product ID" name="id" value="${product.id}"/><br>
                    <form:label path="name">Product name:</form:label><br>
                    <form:input path="name" type="text" class="form-control" id="name" placeholder="Enter Product name" name="name" value="${product.name}"/>
                    <form:errors path="name" cssClass="error"/><br>
                    <form:label path="price">Product price:</form:label><br>
                    <form:input path="price" type="number" class="form-control" id="name" placeholder="Enter Product price" name="name" value="${product.price}"/>
                    <form:errors path="price" cssClass="error"/><br><br>
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
