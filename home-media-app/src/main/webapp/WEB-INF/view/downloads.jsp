<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Downloads</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container mt-3">
            <div class="row">
                <div class="col">
                    <app:torrent-list torrents="${downloads}"/>
                </div>
            </div>
        </div>
    </body>
</html>
