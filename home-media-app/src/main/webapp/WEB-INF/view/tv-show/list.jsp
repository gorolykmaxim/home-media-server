<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>TV Shows</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container">
            <c:forEach var="tvShow" items="${tvShowList}">
                <c:url var="deleteUrl" value="/tv-show/${tvShow.id}/delete"/>
                <c:url var="tvShowUrl" value="/tv-show/${tvShow.id}"/>
                <div class="card mb-3">
                    <img src="${tvShow.thumbnail}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${tvShow.name}</h5>
                        <a href="${tvShowUrl}" class="btn btn-primary">Open</a>
                        <a href="${deleteUrl}" class="btn btn-danger">Delete</a>
                    </div>
                </div>
            </c:forEach>
            <a href="${addShowUrl}" class="btn btn-primary btn-block">Add</a>
        </div>
    </body>
</html>
