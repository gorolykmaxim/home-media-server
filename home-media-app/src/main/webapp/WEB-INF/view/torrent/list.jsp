<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<c:url var="downloadTorrentFormUrl" value="/torrent/download"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Torrents</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <div class="p-3">
            <a class="btn btn-primary" href="${downloadTorrentFormUrl}" role="button">Download torrent</a>
        </div>
        <app:torrent-list torrents="${torrentList}" deleteUrlPrefix="${torrentDeleteUrlPrefix}"/>
    </body>
</html>