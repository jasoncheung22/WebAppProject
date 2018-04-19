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
                        <a class="nav-link" href="<c:url value="/item/create" />">Create Item<span class="sr-only">(current)</span></a>
                    </li>
                </ul>
                <security:authorize access = "isAnonymous()">
                    <button class="btn btn-dark" type="button" onclick="window.location.href = '<c:url value="/login" />'">Login</button>
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
                <h1 class="display-4">Item #${itemId}: <c:out value="${item.subject}" /></h1>
                <security:authorize access = "!isAnonymous()">
                    <p class="lead">
                        <security:authorize access="hasRole('ADMIN') or principal.username=='${item.customerName}'">
                            [<a href="<c:url value="/item/edit/${itemId}" />">Edit</a>]
                        </security:authorize>
                        <security:authorize access="hasRole('ADMIN')">
                            [<a href="<c:url value="/item/delete/${itemId}" />">Delete</a>]
                        </security:authorize>
                    </p>
                </security:authorize>
                <br /><br />
                <p class="lead">
                <i>Customer Name - <c:out value="${item.customerName}" /></i><br /><br />
                <c:out value="${item.body}" /><br /><br /></p>
                <c:if test="${fn:length(item.attachments) > 0}">
                    Attachments:
                    <c:forEach items="${item.attachments}" var="attachment"
                               varStatus="status">
                        <c:if test="${!status.first}">, </c:if>
                        <a href="<c:url value="/item/${item.id}/attachment/${attachment.name}" />">
                            <c:out value="${attachment.name}" /></a>
                    </c:forEach><br /><br />
                </c:if>
                <a href="<c:url value="/item" />">Return to list items</a>
            </div>      
        </div>  
    </body>
</html>
