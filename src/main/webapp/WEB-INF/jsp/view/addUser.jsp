<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
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
                <h2>Registration</h2>
                <form:form method="POST" enctype="multipart/form-data"
                           modelAttribute="bidUser">
                    <form:label path="username">Username</form:label><br/>
                    <form:input type="text" path="username" /><br/><br/>
                    <form:label path="password">Password</form:label><br/>
                    <form:input type="text" path="password" /><br/><br/>
                    <form:hidden path="roles" value="ROLE_USER"/>
                    <br /><br />
                    <input type="submit" class="btn btn-primary" value="Add User"/>
                </form:form>
            </div>
        </div>
    </body>
</html>