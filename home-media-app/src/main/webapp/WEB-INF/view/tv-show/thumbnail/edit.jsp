<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Add thumbnail</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container">
            <img src="${thumbnailUrl}" class="img-fluid rounded mb-3">
            <c:if test="${not empty thumbnailNotFoundMessage}">
                <div class="alert alert-danger mb-3">
                    <p>${thumbnailNotFoundMessage}</p>
                </div>
            </c:if>
            <form action="${submitUrl}" method="post">
                <input type="hidden" name="thumbnail" value="${thumbnailUrl}">
                <button type="submit" class="btn btn-primary">Save</button>
                <a href="${nextThumbnailUrl}" class="btn btn-secondary">Pick a different one</a>
                <a href="${cancelUrl}" class="btn btn-light">Cancel</a>
            </form>
        </div>
    </body>
</html>
