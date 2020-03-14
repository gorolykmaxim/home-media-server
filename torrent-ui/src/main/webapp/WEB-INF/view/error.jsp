<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Whoops...</title>
        <app:header></app:header>
    </head>
    <body class="app-background-color">
        <app:navigation></app:navigation>
        <div class="mdc-top-app-bar--fixed-adjust">
            <div class="mdc-layout-grid">
                <div class="mdc-layout-grid__inner">
                    <jsp:include page="error-component.jsp">
                        <jsp:param name="error" value="${error}"/>
                        <jsp:param name="stackTrace" value="${stackTrace}"/>
                    </jsp:include>
                </div>
            </div>
        </div>
    </body>
</html>