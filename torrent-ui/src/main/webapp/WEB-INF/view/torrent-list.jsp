<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="list-group">
    <c:forEach var="torrent" items="${torrents}">
        <c:url var="deleteUrl" value="${deleteUrlPrefix}/${torrent.id}"/>
        <div class="list-group-item">
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1 app-truncate">${torrent.name}</h5>
            </div>
            <div class="d-flex w-100 justify-content-between">
                <p class="mb-1">${torrent.downloadSpeed}</p>
                <p class="mb-1">${torrent.size}</p>
            </div>
            <div class="d-flex w-100">
                <small>Status: ${torrent.state}</small>
            </div>
            <c:if test="${not torrent.complete}">
                <small>Time left: ${torrent.timeRemaining}</small>
            </c:if>
            <div class="d-flex w-100 justify-content-between">
                <div class="d-block w-100 mt-2 mr-3">
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" style="width: ${torrent.progress}" aria-valuemin="0" aria-valuemax="100"></div>
                    </div>
                </div>
                <a class="btn btn-danger" href="${deleteUrl}" role="button">Delete</a>
            </div>
        </div>
    </c:forEach>
</div>