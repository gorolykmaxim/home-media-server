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
            <div class="row">
                <div class="col">
                    <h3>${tvShow.name}</h3>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <img src="${tvShow.thumbnail}" class="img-fluid">
                </div>
            </div>
            <div class="row mb-3 mt-3">
                <div class="col">
                    <a href="${episodeAddUrl}" class="btn btn-primary">Add episode</a>
                    <a href="${editTvShowUrl}" class="btn btn-secondary">Edit</a>
                    <a href="${deleteTvShowUrl}" class="btn btn-danger">Delete show</a>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <ul class="list-group">
                        <c:forEach var="episode" items="${episodeList}">
                            <c:url var="deleteEpisodeUrl" value="/tv-show/${tvShow.id}/episode/delete">
                                <c:param name="name" value="${episode.name}"/>
                            </c:url>
                            <li class="list-group-item flex-column">
                                <p class="mb-2 app-truncate">${episode.name}</p>
                                <div class="d-flex w-100 justify-content-between">
                                    <c:choose>
                                        <c:when test="${episode.viewed}">
                                            <span class="badge badge-primary badge-pill">viewed</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-success badge-pill">new</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="${deleteEpisodeUrl}"><i class="fas fa-trash-alt text-danger"></i></a>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </body>
</html>
