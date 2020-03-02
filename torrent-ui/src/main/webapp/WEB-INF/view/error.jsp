<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Whoops...</title>
        <app:header></app:header>
    </head>
    <body>
        <app:navigation></app:navigation>
        <jsp:include page="error-component.jsp">
            <jsp:param name="error" value="${error}"/>
            <jsp:param name="stackTrace" value="${stackTrace}"/>
        </jsp:include>
    </body>
</html>