<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Torrents</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation downloadUrl="${downloadTorrentFormUrl}"></app:navigation>
        <div class="container mt-3">
            <div class="row mt-3">
                <div class="col">
                    <app:torrent-list torrents="${torrentList}" deleteUrlPrefix="${torrentDeleteUrlPrefix}"/>
                </div>
            </div>
        </div>
    </body>
</html>