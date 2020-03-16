<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Watched videos</title>
    </head>
    <body style="height: 100%; background-image: url('/thumbnail/${group.latestWatchedVideo.id}')">
        <ul>
            <c:forEach var="notification" items="${notifications}">
                <li>${notification.content}</li>
            </c:forEach>
        </ul>
        <h3>${group.id}, ${group.name}</h3>
        <a href="/">Back</a>
        <a href="/clear-watch-history-for-group/${groupId}">Clear watch history</a>
        <div>
            <c:forEach var="video" items="${group.videos}">
                <p>${video.name}, ${video.timePlayed}, ${video.totalTime}, ${video.lastPlayedDate}</p>
            </c:forEach>
        </div>
    </body>
</html>
