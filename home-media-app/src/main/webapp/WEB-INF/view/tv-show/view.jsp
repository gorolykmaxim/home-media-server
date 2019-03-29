<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>${tvShow.name}</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <h1>${tvShow.name}</h1>
        <img src="${tvShow.thumbnail}">
        <a href="${editTvShowUrl}">Edit</a>
        <c:forEach var="episode" items="${episodeList}">
            <c:url var="deleteEpisodeUrl" value="/tv-show/${tvShow.id}/episode/${episode.name}/delete"/>
            <p>${episode.name} ${episode.viewed}<a href="${deleteEpisodeUrl}">Delete</a></p>
        </c:forEach>
        <a href="${episodeAddUrl}">Add episode</a>
    </body>
</html>
