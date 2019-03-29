<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>${actionType} TV Show</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <form action="${submitUrl}" method="post">
            <label for="name">TV Show name:</label>
            <input type="text" id="name" name="name" value="${name}">
            <button type="submit">Next</button>
            <a href="${cancelUrl}">Cancel</a>
        </form>
    </body>
</html>
