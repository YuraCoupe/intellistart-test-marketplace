<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

           <nav class="navbar navbar-inverse">
              <div class="container">
                <div class="navbar-header">
                  <a class="navbar-brand" href="/">IntelliStart Test Marketplace</a>
                </div>
                <ul class="nav navbar-nav">
                  <security:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
                  <li><a href="/">Home</a></li>
                  <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="/products">Products<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                      <li><a href="/products">Find</a></li>
                      <li><a href="/products/new">New</a></li>
                    </ul>
                  </li>
                  </security:authorize>
                  <security:authorize access="hasRole('ROLE_ADMIN')">
                  <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="/users">Users<span class="caret"></span></a>
                   <ul class="dropdown-menu">
                     <li><a href="/users">Find</a></li>
                     <li><a href="/users/new">New</a></li>
                   </ul>
                  </li>
                  </security:authorize>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a style="float: right" href="/logout">Logout</a>
                    </li>
                </ul>
              </div>
            </nav>