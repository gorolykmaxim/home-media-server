<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Add thumbnail</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <img src="${thumbnailUrl}"/>
        <c:if test="${not empty thumbnailNotFoundMessage}">
            <p>${thumbnailNotFoundMessage}</p>
        </c:if>
        <form action="${submitUrl}" method="post">
            <input type="hidden" name="thumbnail" value="${thumbnailUrl}">
            <button type="submit">Save</button>
            <a href="${nextThumbnailUrl}">Pick a different one</a>
            <a href="${cancelUrl}">Cancel</a>
        </form>
    </body>
</html>
