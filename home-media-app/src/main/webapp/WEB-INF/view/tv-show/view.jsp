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
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            ${episode.name}
                            <c:if test="${episode.viewed}">
                                <span class="badge badge-primary badge-pill">viewed</span>
                            </c:if>
                        </div>
                        <a href="${deleteEpisodeUrl}"><i class="fas fa-trash-alt text-danger"></i></a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </body>
</html>
