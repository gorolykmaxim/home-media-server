<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>TV Shows</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container mt-3">
            <div class="card-columns">
                <c:forEach var="tvShow" items="${tvShowList}">
                    <c:url var="tvShowUrl" value="/tv-show/${tvShow.id}"/>
                    <a href="${tvShowUrl}" class="app-clickable-card">
                        <div class="card mb-3 text-white">
                            <img src="${tvShow.thumbnail}" class="card-img">
                            <div class="card-img-overlay app-text-above-image-container">
                                <h5 class="card-title">${tvShow.name}</h5>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <div class="row">
                <div class="col">
                    <a href="${addShowUrl}" class="btn btn-primary btn-block">Add</a>
                </div>
            </div>
        </div>
    </body>
</html>
