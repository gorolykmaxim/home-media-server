<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Watched videos</title>
    </head>
    <body>
        <ul>
            <c:forEach var="notification" items="${notifications}">
                <li>${notification.content}</li>
            </c:forEach>
        </ul>
        <c:forEach var="group" items="${groups}">
            <h3>${group.id}, ${group.name}</h3>
            <a href="/${group.id}">History</a>
            <p>${group.latestWatchedVideo.name}, ${group.latestWatchedVideo.timePlayed}, ${group.latestWatchedVideo.totalTime}, ${group.latestWatchedVideo.lastPlayedDate}</p>
            <img src="/thumbnail/${group.latestWatchedVideo.id}"/>
        </c:forEach>
    </body>
</html>
