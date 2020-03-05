<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Watched videos</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <div class="container mt-3">
            <div class="row">
                <div class="col">
                    <c:if test="${!empty notifications}">
                        <div class="alert alert-danger">
                            <h4 class="alert-heading">Warning!</h4>
                            <c:forEach var="notification" items="${notifications}">
                                <p>${notification.content}</p>
                                <hr>
                            </c:forEach>
                            <form method="post" action="/discard-notifications">
                                <button type="submit" class="btn btn-light">Discard</button>
                            </form>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${empty groups}">
                            <p class="mb-0">Nothing...</p>
                            <h6>Go watch some stuff!</h6>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="group" items="${groups}">
                                <c:choose>
                                    <c:when test="${fn:length(groups) gt 1}">
                                        <div class="accordion" id="watched-recently-accordion">
                                            <app:video-group group="${group}" parentContainer="watched-recently-accordion"></app:video-group>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <app:video-group group="${group}"></app:video-group>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </body>
</html>
