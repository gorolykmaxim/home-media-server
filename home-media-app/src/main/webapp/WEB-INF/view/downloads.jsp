<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Downloads</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <app:torrent-list torrents="${downloads}"/>
    </body>
</html>
