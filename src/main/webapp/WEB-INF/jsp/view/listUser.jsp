<!DOCTYPE html>
<html>
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
                    <security:authorize access="hasRole('ADMIN')">
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/user" />">Manage User Accounts</a><span class="sr-only">(current)</span>
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
    <a href="<c:url value="/item" />">Return to list items</a>
    <h2>Users</h2>
    <c:choose>
      <c:when test="${fn:length(bidUsers) == 0}">
        <i>There are no users in the system.</i>
      </c:when>
      <c:otherwise>
        <table>
          <tr>
            <th>Username</th><th>Password</th><th>Roles</th><th>Action</th>
          </tr>
          <c:forEach items="${bidUsers}" var="user">
            <tr>
              <td>${user.username}</td><td>${user.password}</td>
              <td>
                <c:forEach items="${user.roles}" var="role" varStatus="status">
                  <c:if test="${!status.first}">, </c:if>
                  ${role.role}
                </c:forEach>
              </td>
              <td>
                [<a href="<c:url value="/user/delete/${user.username}" />">Delete</a>]
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>
  </body>
</html>