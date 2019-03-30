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
        <div class="container mt-3">
            <div class="row">
                <div class="col">
                    <a class="btn btn-primary" href="${downloadTorrentFormUrl}" role="button">Download torrent</a>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col">
                    <app:torrent-list torrents="${torrentList}" deleteUrlPrefix="${torrentDeleteUrlPrefix}"/>
                </div>
            </div>
        </div>
    </body>
</html>