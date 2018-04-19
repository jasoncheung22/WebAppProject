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
                    <li class="nav-item active">
                        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/item/create" />">Create Item</a>
                    </li>

                    <security:authorize access="hasRole('ADMIN')">
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/user" />">Manage User Accounts</a>
                        </li>
                    </security:authorize>

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
            <div class="jumbotron jumbotron-fluid">
                <div class="container">
                    <h1 class="display-4">Items</h1>
                    <c:choose>
                        <c:when test="${fn:length(itemDatabase) == 0}">
                            <p class="lead"><i>There are no items in the system.</i></p> 
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${itemDatabase}" var="item">
                                Ticket ${item.id}:
                                <a href="<c:url value="/item/view/${item.id}" />">
                                    <c:out value="${item.subject}" /></a>
                                (customer: <c:out value="${item.customerName}" />)
                                <security:authorize access = "!isAnonymous()">
                                <security:authorize access="hasRole('ADMIN') or
                                                    principal.username=='${item.customerName}'">
                                    [<a href="<c:url value="/item/edit/${item.id}" />">Edit</a>]
                                </security:authorize>
                                <security:authorize access="hasRole('ADMIN') or principal.username=='${item.customerName}'">
                                    [<a href="<c:url value="/item/delete/${item.id}" />">Delete</a>]
                                </security:authorize>
                                    </security:authorize>
                                <br /><br />
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </body>
</html>
