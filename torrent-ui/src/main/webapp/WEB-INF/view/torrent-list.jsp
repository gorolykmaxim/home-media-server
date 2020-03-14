<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="torrent" items="${torrents}">
    <c:url var="deleteUrl" value="${deleteUrlPrefix}/${torrent.id}"/>
    <div class="mdc-layout-grid__cell--span-12">
        <div class="mdc-card">
            <div class="mdc-card__primary-action app-padding">
                <h1 class="mdc-typography mdc-typography--subtitle2">${torrent.name}</h1>
                <span class="mdc-typography mdc-typography--body2">Size: ${torrent.size}</span>
                <span class="mdc-typography mdc-typography--body2">Status: ${torrent.state}</span>
                <c:if test="${not torrent.complete}">
                    <span class="mdc-typography mdc-typography--body2">Speed: ${torrent.downloadSpeed}</span>
                    <span class="mdc-typography mdc-typography--body2">Time left: ${torrent.timeRemaining}</span>
                    <br/>
                    <div class="mdc-linear-progress">
                        <div class="mdc-linear-progress__buffering-dots"></div>
                        <div class="mdc-linear-progress__buffer"></div>
                        <div class="mdc-linear-progress__bar mdc-linear-progress__primary-bar" style="transform: scaleX(${torrent.progress})">
                            <span class="mdc-linear-progress__bar-inner"></span>
                        </div>
                        <div class="mdc-linear-progress__bar mdc-linear-progress__secondary-bar">
                            <span class="mdc-linear-progress__bar-inner"></span>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="mdc-card__actions">
                <div class="mdc-card__action-buttons">
                    <a class="mdc-button mdc-card__action mdc-card__action--button" href="${deleteUrl}">
                        <div class="mdc-button__ripple"></div>
                        <span class="mdc-button__label">Delete</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</c:forEach>