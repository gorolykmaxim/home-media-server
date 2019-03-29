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
        <c:forEach var="tvShow" items="${tvShowList}">
            <c:url var="deleteUrl" value="/tv-show/${tvShow.id}/delete"/>
            <c:url var="tvShowUrl" value="/tv-show/${tvShow.id}"/>
            <a href="${tvShowUrl}">${tvShow.name}</a>
            <img src="${tvShow.thumbnail}">
            <a href="${deleteUrl}">Delete</a>
        </c:forEach>
        <c:url var="addShowUrl" value="/tv-show/add"/>
        <a href="${addShowUrl}">Add</a>
    </body>
</html>
