<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>${tvShow.name}</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container">
            <h3>${tvShow.name}</h3>
            <img src="${tvShow.thumbnail}" class="img-fluid mb-3">
            <a href="${episodeAddUrl}" class="btn btn-primary">Add episode</a>
            <a href="${editTvShowUrl}" class="btn btn-secondary">Edit</a>
            <ul class="list-group mt-3">
                <c:forEach var="episode" items="${episodeList}">
                    <c:url var="deleteEpisodeUrl" value="/tv-show/${tvShow.id}/episode/${episode.name}/delete"/>
                    <li class="list-group-item flex-column">
                        <p class="mb-1 app-truncate">${episode.name}</p>
                        <div class="d-flex w-100 justify-content-between">
                            <c:choose>
                                <c:when test="${episode.viewed}">
                                    <span class="badge badge-primary badge-pill">viewed</span>
                                </c:when>
                                <c:otherwise>
                                    <span></span>
                                </c:otherwise>
                            </c:choose>
                            <a href="${deleteEpisodeUrl}"><i class="fas fa-trash-alt text-danger"></i></a>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </body>
</html>
