<%@taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>${actionType} TV Show</title>
        <app:header/>
    </head>
    <body>
        <app:navigation/>
        <div class="container">
            <form action="${submitUrl}" method="post">
                <div class="form-group">
                    <label for="name">TV Show name:</label>
                    <input type="text" id="name" name="name" value="${name}" class="form-control">
                </div>
                <button type="submit" class="btn btn-primary">Next</button>
                <a href="${cancelUrl}" class="btn btn-light">Cancel</a>
            </form>
        </div>
    </body>
</html>
