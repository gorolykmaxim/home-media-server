<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>Add episode of ${tvShow.name}</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container">
            <form action="${addEpisodeUrl}" method="post">
                <div class="form-group">
                    <label for="magnetUri">Magnet URI:</label>
                    <input type="text" id="magnetUri" name="magnetUri" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary">Download</button>
                <a href="${cancelUrl}" class="btn btn-light">Cancel</a>
            </form>
        </div>
    </body>
</html>
