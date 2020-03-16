<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Watched videos</title>
        <app:header includeBootstrap="${true}"/>
    </head>
    <body class="app-background-color">
        <app:navigation/>
        <div class="mdc-top-app-bar--fixed-adjust app-full-screen">
<%--            <ul>--%>
<%--                <c:forEach var="notification" items="${notifications}">--%>
<%--                    <li>${notification.content}</li>--%>
<%--                </c:forEach>--%>
<%--            </ul>--%>
            <app:video-group-carousel groups="${groups}"/>
<%--            <c:forEach var="group" items="${groups}">--%>
<%--                <h3>${group.id}, ${group.name}</h3>--%>
<%--                <a href="/${group.id}">History</a>--%>
<%--                <p>${group.latestWatchedVideo.name}, ${group.latestWatchedVideo.timePlayed}, ${group.latestWatchedVideo.totalTime}, ${group.latestWatchedVideo.lastPlayedDate}</p>--%>
<%--                <img src="/thumbnail/${group.latestWatchedVideo.id}"/>--%>
<%--            </c:forEach>--%>
        </div>
    </body>
</html>
