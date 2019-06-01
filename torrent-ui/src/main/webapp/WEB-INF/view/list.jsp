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
                <div id="torrent-list" class="col">
                </div>
            </div>
        </div>
        <input id="refreshInterval" type="hidden" value="${refreshInterval}">
        <script type="application/javascript">
            $(document).ready(function () {
                var $container = $('#torrent-list');
                var interval = $('#refreshInterval').val();
                function updateTorrents() {
                    $.get('/list', function (data) {
                        $container.html(data);
                    });
                }
                setInterval(updateTorrents, interval);
                updateTorrents();
            });
        </script>
    </body>
</html>