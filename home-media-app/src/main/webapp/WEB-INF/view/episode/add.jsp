<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Add episode of ${tvShow.name}</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <form action="${addEpisodeUrl}" method="post">
            <label for="magnetUri">Magnet URI:</label>
            <input type="text" id="magnetUri" name="magnetUri">
            <button type="submit">Download</button>
            <a href="${cancelUrl}">Cancel</a>
        </form>
    </body>
</html>
