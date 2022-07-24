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
            <form action="/products/edit">
                <div class="form-group">
                    <label for="Id">Product name:</label><br>
                    <select class="form-control" id="id" name="id">
                        <option disabled selected value> -- select product -- </option>
                        <c:forEach items="${products}" var="product">
                            <option value="${product.id}"><c:out value="${product.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
                    <input type="submit" value="Search">
            </form><br>

            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group me-2" role="group" aria-label="Second group">
                   <a href="/products/new" type="button" class="btn btn-primary">New</a>
                </div>
                <div class="btn-group me-2" role="group" aria-label="Second group">
                    <a href="/products/" type="button" class="btn btn-success">All</a>
                    <a href="/products/checkout" type="button" class="btn btn-info">Checkout</a>
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
                        <td>id</td>
                        <td>name</td>
                        <td>price</td>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>
                            <c:out value="${product.id}"/>
                        </td>
                        <td>
                            <c:out value="${product.name}"/>
                        </td>
                        <td>
                            <c:out value="${product.price}"/><br>
                        </td>
                        <td>
                            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                     <a href="/products/buy/${product.id}" type="button" class="btn btn-success">Buy</a>
                                     <a href="/products/edit/${product.id}" type="button" class="btn btn-warning">Edit</a>
                                     <a href="/products/delete/${product.id}" type="button" class="btn btn-danger">Remove</a>
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
