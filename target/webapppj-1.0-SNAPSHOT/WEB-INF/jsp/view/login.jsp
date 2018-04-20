<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support Login</title>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <a class="navbar-brand" href="#">Bid You Like</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/item" />">Home</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="<c:url value="/item/create" />">Create Item</a>
                    </li>
                </ul>
                <security:authorize access = "isAnonymous()">
                    <button class="btn btn-dark" type="button" onclick="window.location.href = '<c:url value="/login" />'">Login</button>
                    <button class="btn btn-dark" type="button" onclick="window.location.href = '<c:url value="/user/create" />'">Registration</button>
                </security:authorize>
                <security:authorize access = "!isAnonymous()">
                    <c:url var="logoutUrl" value="/logout"/>
                    <form action="${logoutUrl}" method="post">
                        <input class="btn btn-dark" type="submit" value="Log out" />
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </security:authorize>
            </div>
        </nav>

        <div class="container">
            <div class="jumbotron">
                <c:if test="${param.error != null}">
                    <p>Login failed.</p>
                </c:if>
                <c:if test="${param.logout != null}">
                    <p>You have logged out.</p>
                </c:if>

                <h2>Login Your Account</h2>
                <form action="login" method="POST">
                    <div class="form-group">
                        <label for="username">Username:</label><br/>
                        <input type="text" id="username" name="username" /><br/><br/>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label><br/>
                        <input type="password" id="password" name="password" /><br/><br/>
                    </div>
                    <input type="checkbox" id="remember-me" name="remember-me" />
                    <label for="remember-me">Remember me</label><br/><br/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input class="btn btn-outline-success" type="submit" value="Log In"/>
                </form>
            </div>
        </div>
    </body>
</html>
