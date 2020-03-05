<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="group" type="com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModel" %>
<%@attribute name="parentContainer" type="java.lang.String" %>

<div class="card">
    <div class="card-header">
        <div class="d-flex justify-content-between">
            <h2 class="mb-0 w-50">
                <button class="btn btn-link collapsed app-video-group-name w-100" type="button" data-toggle="collapse" data-target="#collapse-${group.id}">
                    ${group.name}
                </button>
            </h2>
            <a class="btn btn-outline-danger app-accordion-button-height-fixture" href="/clear-watch-history-for-group/${group.name}">Clear watch history</a>
        </div>
    </div>
    <div id="collapse-${group.id}" class="collapse" <c:if test="${parentContainer}">data-parent="#${parentContainer}"</c:if>>
        <div class="card-body app-accordion-card-body-padding-fixture">
            <div class="list-group">
                <c:forEach var="video" items="${group.videos}">
                    <div class="list-group-item list-group-item-action">
                        <p class="mb-1 app-truncate">${video.name}</p>
                        <p class="mb-0">
                            <small>${video.lastPlayedDate}</small>
                        </p>
                        <small>Played ${video.timePlayed} of ${video.totalTime}</small>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>