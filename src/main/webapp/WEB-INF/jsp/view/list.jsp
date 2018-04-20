<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <link rel="stylesheet" href="<c:url value="/resources/static/css/thumbnail-gallery.css" />">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/baguettebox.js/1.8.1/baguetteBox.min.js"></script>
    <script>
        baguetteBox.run('.tz-gallery');
    </script>
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
                <div class="container gallery-container">
                    <h1 class="display-4">Items</h1>
                    <c:choose>
                        <c:when test="${fn:length(itemDatabase) == 0}">
                            <p class="lead"><i>There are no items in the system.</i></p> 
                        </c:when>
                        <c:otherwise>
                            <div class="tz-gallery">
                            <div class="row">
                                <c:forEach items="${itemDatabase}" var="item">
                                    <div class="col-sm-6 col-md-4">
                                        <div class="thumbnail">
                                            <a class="lightbox" href="<c:url value="/item/view/${item.id}" />">
                                                <c:choose>
                                                    <c:when test="${fn:length(item.attachments) > 0}">
                                                        <img src="<c:url value="/item/${item.id}/attachment/${item.attachments[0].name}" />" style="width:100%"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="<c:url value="/resources/images/empty.png" />" style="width:100%"/>  
                                                    </c:otherwise>
                                                </c:choose>
                                                <div class="caption">
                                                    <h3>Item ${item.id}</h3>
                                                    <p>Owner: <c:out value="${item.customerName}" /></p>
                                                    <p>Item Name: <c:out value="${item.subject}" /></p>
                                                    <p>Description: <c:out value="${item.body}" /></p>
                                                </div>
                                            </a>
                                            <security:authorize access = "!isAnonymous()">
                                                <security:authorize access="hasRole('ADMIN')">
                                                    <button type="button" class="btn btn-primary" onclick="window.location.href = '<c:url value="/item/delete/${item.id}" />'">Delete</button>
                                                </security:authorize>
                                            </security:authorize>
                                        </div>
                                    </div>
                                    <br /><br />
                                </c:forEach>
                                    </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            </div>
    </body>
</html>
