<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="torrentUrl" value="/torrent"/>


<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Home Media Server</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${torrentUrl}">Torrents</a>
            </li>
        </ul>
    </div>
</nav>