<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="groups" type="java.lang.Iterable" %>

<div id="video-group-carousel" class="carousel slide app-full-screen" data-ride="carousel">
    <ol class="carousel-indicators">
        <c:forEach var="group" items="${groups}" varStatus="status">
            <li data-target="#video-group-carousel" data-slide-to="${status.index}" class="${status.index == 0 ? 'active' : ''}"></li>
        </c:forEach>
    </ol>
    <div class="carousel-inner app-full-screen">
        <c:forEach var="group" items="${groups}" varStatus="status">
            <div class="carousel-item <c:if test="${status.index == 0}">active</c:if> app-full-screen app-background-image" style="background-image: url('/thumbnail/${group.latestWatchedVideo.id}')">
                <div class="carousel-caption">
                    <h3 class="app-break-text">${group.latestWatchedVideo.name}</h3>
                    <h5 class="app-break-text">${group.name}</h5>
                    <p>${group.latestWatchedVideo.lastPlayedDate}</p>
                    <a class="mdc-button mdc-button--raised" href="/${group.id}">
                        <span class="mdc-button__ripple"></span>
                        <span class="mdc-button__label">History</span>
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>
    <a class="carousel-control-prev" href="#video-group-carousel" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#video-group-carousel" role="button" data-slide="next">
        <span class="carousel-control-next-icon"></span>
        <span class="sr-only">Next</span>
    </a>
</div>