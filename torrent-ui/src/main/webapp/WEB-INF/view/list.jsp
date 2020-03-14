<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Torrents</title>
        <app:header></app:header>
    </head>
    <body class="app-background-color">
        <app:navigation></app:navigation>
        <div class="mdc-top-app-bar--fixed-adjust">
            <div class="mdc-layout-grid">
                <div id="torrent-list" class="mdc-layout-grid__inner"></div>
            </div>
        </div>
        <a class="mdc-fab app-fab--absolute" href="/download">
            <div class="mdc-fab__ripple"></div>
            <span class="mdc-fab__icon material-icons">add</span>
        </a>
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