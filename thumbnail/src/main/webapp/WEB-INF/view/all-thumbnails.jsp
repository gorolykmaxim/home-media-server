<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Thumbnail</title>
        <app:header></app:header>
    </head>
    <body class="app-background-color">
        <app:navigation></app:navigation>
        <div class="mdc-top-app-bar--fixed-adjust">
            <div class="mdc-layout-grid">
                <div class="mdc-layout-grid__inner">
                    <c:forEach var="thumbnail" items="${thumbnailNames}">
                        <div class="mdc-layout-grid__cell">
                            <div class="mdc-card">
                                <div class="mdc-card__media mdc-card__media--16-9" style="background-image: url('/api/thumbnail-image?name=${thumbnail}')"></div>
                                <div class="mdc-card__actions">
                                    <span class="mdc-typography mdc-typography--subtitle1">${thumbnail}</span>
                                    <div class="mdc-card__action-icons">
                                        <a class="mdc-icon-button material-icons mdc-card__action mdc-card__action--icon" href="/thumbnail-removal-confirmation?thumbnailName=${thumbnail}">delete</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>