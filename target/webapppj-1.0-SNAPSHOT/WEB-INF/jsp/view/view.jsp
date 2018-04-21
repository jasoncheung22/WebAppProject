<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <script src="<c:url  value="/resources/static/js/bootstrap-magnify.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url  value="/resources/static/css/bootstrap-magnify.min.css"/>">
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
                    <security:authorize access="hasRole('ADMIN')">
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/user" />">Manage User Accounts</a>
                        </li>
                    </security:authorize>
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
                <div class="row">
                    <div class="col-sm">
                        <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                            <ol class="carousel-indicators">
                                <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                                <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                                <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                            </ol>
                            <c:choose>
                                <c:when test="${fn:length(item.attachments) > 0}">
                                    <div class="carousel-inner">
                                        <c:forEach items="${item.attachments}" var="attachment" varStatus="status">
                                            <c:choose>
                                                <c:when test="${status.first}">                                  
                                                    <div class="carousel-item active">
                                                        <img class="d-block w-100" data-toggle="magnify" src="<c:url value="/item/${item.id}/attachment/${attachment.name}" />">
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="carousel-item">
                                                        <img class="d-block w-100" data-toggle="magnify" src="<c:url value="/item/${item.id}/attachment/${attachment.name}" />">
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="carousel-inner">
                                        <div class="carousel-item active">
                                            <img class="d-block w-100" data-toggle="magnify" src="<c:url value="/resources/images/empty.png" />">
                                        </div>
                                        <div class="carousel-item">
                                            <img class="d-block w-100" data-toggle="magnify" src="<c:url value="/resources/images/empty.png" />">
                                        </div>
                                        <div class="carousel-item">
                                            <img class="d-block w-100" data-toggle="magnify" src="<c:url value="/resources/images/empty.png" />">
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span class="sr-only">Previous</span>
                            </a>
                            <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                <span class="sr-only">Next</span>
                            </a>
                        </div>
                    </div>
                    <div class="col-sm">
                        <security:authorize access = "!isAnonymous()">
                            <p class="lead">
                                <security:authorize access="hasRole('ADMIN')">
                                    <button class="btn btn-danger" type="button" onclick="window.location.href = '<c:url value="/item/delete/${itemId}" />'">Delete</button>
                                </security:authorize>
                            </p>
                        </security:authorize>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th scope="col" colspan="3"><h4 class="display-4">Item Name:<c:out value="${item.subject}" /></h4></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th scope="row">Owner : </th>
                                    <td colspan="2"><c:out value="${item.customerName}" /></td>
                                </tr>
                                <tr>
                                    <th scope="row">Buy it now :$</th>
                                    <td colspan="2"><c:out value="${item.price}" /></td>
                                </tr>
                                <tr>
                                    <th scope="row">Currency Bid :$</th>
                                    <td> <c:out value="${item.bidprice}" /></td>
                                    <c:choose>
                                        <c:when test="${fn:length(item.bidRecord) <=0 }">
                                            <td>No bid yet!</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
                                                    <c:out value="${fn:length(item.bidRecord)}"/> bids
                                                </button></td>
                                            </c:otherwise>
                                        </c:choose>
                                </tr>
                                <tr>
                                    <th scope="row">Currency Bid Status: </th>
                                    <td>                            
                                        <c:choose>
                                            <c:when test="${item.status == 0}">
                                                <p class="lead">The Bid is ended by owner.</p>
                                            </c:when>
                                            <c:when test="${item.status == 1}">
                                                <p class="lead">The Bid is running now!!</p>
                                            </c:when>
                                            <c:when test="${item.status == 2}">
                                                <c:out value="The Bid is end! Winner is ${item.bidusername}" />
                                            </c:when>
                                        </c:choose></td>
                                </tr>
                            </tbody>
                        </table>

                        <br /><br />
                        <div class="accordion" id="accordion">
                            <div class="card">
                                <div class="card-header" id="headingOne">
                                    <h5 class="mb-0">
                                        <button class="btn btn-link btn-lg btn-block" type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
                                            Description
                                        </button>
                                    </h5>
                                </div>
                                <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordion">
                                    <div class="card-body">
                                        <c:out value="${item.body}" />
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="card-header" id="headingOne">
                                    <h5 class="mb-0">
                                        <button class="btn btn-link btn-lg btn-block"class="btn btn-info" type="button" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">                                            Comments
                                        </button>
                                    </h5>
                                </div>
                                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordion">          
                                    <div class="card-body">
                                        <c:choose>
                                            <c:when test = "${fn:length(item.comments) > 0}">
                                                <table class="table table-sm">
                                                    <tbody>
                                                        <c:forEach items="${item.comments}" var="acomment">
                                                            <tr>
                                                                <th scope="row">${acomment.username} say:</th>
                                                                <td>${acomment.comment}</td>
                                                                <security:authorize access="hasRole('ADMIN')">
                                                                    <td><button type="button" class="btn btn-primary" onclick="window.location.href = '<c:url value="${item.id}/comment/delete/${acomment.id}" />'">Remove</button></td>
                                                                </security:authorize>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:when>
                                            <c:otherwise>
                                                No Comment Now!!!!!
                                            </c:otherwise>
                                        </c:choose>
                                        <hr class="my-4">
                                        <security:authorize access = "!isAnonymous()">
                                            Leave your comment here 
                                            <form:form method="POST" enctype="multipart/form-data" modelAttribute="commentForm" action="${item.id}/comment">
                                                <form:textarea path="comment" rows="2" cols="40" /><br/><br/>
                                                <input class="btn btn-primary mb-2" type="submit" value="Leave My Comment"/>
                                            </form:form>
                                        </security:authorize>
                                    </div>
                                </div>
                            </div>
                            <security:authorize access = "!isAnonymous() and principal.username!='${item.customerName}'">
                                <c:if test="${item.status == 1}">
                                    <form method="POST" enctype="multipart/form-data" name="Bidform" action="bid">
                                        <div class="form-group">      
                                            <label name="bidprice">I want to Bid:$</label><br/>
                                            <input type="number" maxlength="20" name="bidprice" /><br/><br/>
                                        </div>
                                        <input type="hidden" name="id" value="${item.id}"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="submit" class="btn btn-primary mb-2" value="Bid"/>
                                    </form>
                                </c:if>
                            </security:authorize>
                            <security:authorize access = "!isAnonymous()">
                                <security:authorize access="principal.username=='${item.customerName}'">
                                    <c:if test="${item.status == 1}">
                                        <form method="POST" enctype="multipart/form-data" name="giveupbid" action="giveupbid">
                                            <input type="hidden" name="id" value="${item.id}"/>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="submit" class="btn btn-danger" value="Give up the Bid"/>
                                        </form>
                                    </c:if>
                                    <c:if test="${item.bidusername != "NULL" and item.status == 1}">
                                        <form method="POST" enctype="multipart/form-data" name="endform" action="endbid">
                                            <input type="hidden" name="id" value="${item.id}"/>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <input type="submit" class="btn btn-danger" value="End Bid with winner"/>
                                        </form>
                                    </c:if>
                                </security:authorize>
                            </security:authorize>

                        </div>
                    </div>      
                </div>  


                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Bid history</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th>User</th>
                                            <th>Price</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${item.bidRecord}" var="aBidRecord">
                                            <tr>
                                                <th scope="row">${aBidRecord.username}</th>
                                                <td>${aBidRecord.price}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>   
                </body>
                </html>
