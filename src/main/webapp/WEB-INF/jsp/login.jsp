<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

 <!doctype html>
 <html lang="en">
   <head>
     <meta charset="utf-8">
     <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
     <meta name="description" content="">
     <meta name="author" content="">
     <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

     <title>Signin Template for Bootstrap</title>

     <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/sign-in/">

     <!-- Bootstrap core CSS -->
     <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

     <!-- Custom styles for this template -->
     <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
   </head>

   <body class="text-center">
     <form class="form-signin" action="/proccess-login" method="POST">
        <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
        <div class="form-group ${error != null ? 'has-error' : ''}">
            <span>${message}</span>
            <label for="username" class="sr-only">Email address</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="username" required autofocus>
            <label for="password" class="sr-only">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
            <span>${error}</span>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            <h4 class="text-center"><a href="${pageContext.request.contextPath}/users/new">Create an account</a></h4>
       </div>
     </form>
   </body>
 </html>